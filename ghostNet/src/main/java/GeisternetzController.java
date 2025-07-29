import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ViewScoped
public class GeisternetzController implements Serializable {

    private static final long serialVersionUID = 1L;

    private int index = 0;

    @Inject
    private GeisternetzDAO geisternetzDAO;
    @Inject
    private GeisternetzListe geisternetzListe;
    @Inject
    private LoginController loginController;
    

    private Geisternetz geisternetz;
    private String fehlermeldung;
    private boolean anonymeMeldung = false;
    private String erfolgsNachricht;
    
    
    private static final List<String> DEFAULT_IMAGES = Arrays.asList(
            "https://www.shutterstock.com/image-vector/vector-illustration-fishnet-fishing-symbol-260nw-1464441224.jpg",
            "https://cdn-icons-png.flaticon.com/512/2083/2083845.png",
            "https://www.shutterstock.com/image-vector/doodle-fish-net-vector-hand-260nw-2136940005.jpg"
        );
        private Random random = new Random();

        private String getRandomImage() {
            return DEFAULT_IMAGES.get(random.nextInt(DEFAULT_IMAGES.size()));
        }
    
    
    

    public Geisternetz getGeisternetz() {
        if (geisternetz == null) {
        	geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
        return geisternetz;
    }

    public void vor() {
        System.err.println("Saving Geisternetz " + geisternetz.getNr());
        EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
        geisternetzDAO.merge(geisternetz);
        t.commit();

        if (index < geisternetzDAO.getGeisternetzCount() - 1) {
            index++;
            geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
    }

    public void zurueck() {
        System.err.println("Saving Geisternetz " + geisternetz.getNr());
        EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
        geisternetzDAO.merge(geisternetz);
        t.commit();

        if (index > 0) {
            index--;
            geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
    }

    // Method with extensive debugging
    public void removeGeisternetz(Geisternetz geisternetzToDelete) {
        System.err.println("===== DELETE METHOD CALLED =====");
        System.err.println("Person to delete: " + (geisternetzToDelete != null ?  " (ID: " + geisternetzToDelete.getNr() + ")" : "NULL"));
        
        if (geisternetzToDelete != null) {
            try {
                System.err.println("Before deletion - Person count: " + geisternetzDAO.getGeisternetzCount());
                
                // Check if person exists in database
                List<Geisternetz> alleGeisternetze = geisternetzDAO.getAllGeisternetze();
                boolean exists = alleGeisternetze.stream().anyMatch(p -> p.getNr() == geisternetzToDelete.getNr());
                System.err.println("Geisternetz exists in database: " + exists);
                
                geisternetzDAO.removeGeisternetz(geisternetzToDelete);
                
                System.err.println("After deletion - Geisternetz count: " + geisternetzDAO.getGeisternetzCount());
                System.err.println("Geisternetz " + geisternetzToDelete.getNr() + " deletion attempt completed.");
                
            } catch (Exception e) {
                System.err.println("ERROR during deletion: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("ERROR: Geisternetz to delete is NULL!");
        }
        System.err.println("===== GEISTERNETZ METHOD END =====");
    }

    // Alternative method without parameters for testing
    public void testDelete() {
        System.err.println("TEST DELETE CALLED - this method has no parameters");
    }

    public int getIndex() {
        return index;
    }

    public int getMaxIndex() {
        return (int) geisternetzDAO.getGeisternetzCount() - 1;
    }

    public List<String> getAlleBilder() {
        return geisternetzDAO.getAlleBilder();
    }
    
    
    
    
    public String erstellung() {
        try {
            setFehlermeldung(null);
            setErfolgsNachricht(null);

            if (!validiereEingaben()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", getFehlermeldung()));
                return null;            }

            Geisternetz neuesGeisternetz = geisternetzListe.getNeuesGeisternetz();

            if (neuesGeisternetz.getNetzStatus() == null) {
                neuesGeisternetz.setNetzStatus(NetzStatus.GEMELDET);
            }

            GeisternetzDAO dao = geisternetzListe.getDao();
            EntityTransaction transaction = dao.getAndBeginTransaction();

            try {
                dao.persist(neuesGeisternetz);

                Person melder = anonymeMeldung ? null : getCurrentUser();
                Meldung meldung = new Meldung(neuesGeisternetz, melder, anonymeMeldung);
                meldung.setMeldungsDatum(LocalDateTime.now());

                dao.persist(meldung);

                transaction.commit();

                geisternetzListe.resetNeuesGeisternetz();
                boolean warAnonym = anonymeMeldung; 
                anonymeMeldung = false;

                String nachricht = warAnonym
                        ? "Geisternetz wurde erfolgreich anonym gemeldet! Vielen Dank für Ihren Beitrag zum Meeresschutz."
                        : "Geisternetz wurde erfolgreich gemeldet! Vielen Dank für Ihren Beitrag zum Meeresschutz.";
                    
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", nachricht));

                    return null;

            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }

        } catch (Exception e) {
        	 e.printStackTrace();
             String errorMsg = "Fehler beim Speichern der Meldung: " + e.getMessage();
             setFehlermeldung(errorMsg);
             
             FacesContext.getCurrentInstance().addMessage(null, 
                 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", errorMsg));
             return null;
        }
    }

    
    
    
    private boolean validiereEingaben() {
        Geisternetz geisternetz = geisternetzListe.getNeuesGeisternetz();

        if (geisternetz.getGroeße() == null || geisternetz.getGroeße().trim().isEmpty()) {
            setFehlermeldung("Bitte geben Sie eine Netzgröße an.");
            return false;
        }

        if (geisternetz.getNetzStatus() == null) {
            geisternetz.setNetzStatus(NetzStatus.GEMELDET);
            System.out.println("NetzStatus automatisch auf GEMELDET gesetzt");
        }

        if (geisternetz.getLat() < -90 || geisternetz.getLat() > 90) {
            setFehlermeldung("Breitengrad muss zwischen -90 und 90 liegen.");
            return false;
        }

        if (geisternetz.getLng() < -180 || geisternetz.getLng() > 180) {
            setFehlermeldung("Längengrad muss zwischen -180 und 180 liegen.");
            return false;
        }

        // Automatisch Bild zuweisen, falls keines angegeben wurde
        if (geisternetz.getBild() == null || geisternetz.getBild().trim().isEmpty()) {
            String randomImage = getRandomImage();
            geisternetz.setBild(randomImage);
            System.out.println("Zufälliges Bild zugewiesen: " + randomImage);
        }

        return true;
    }
    
    
    private Person getCurrentUser() {
        return loginController.getAktuellerNutzer();
    }




	public String getFehlermeldung() {
		return fehlermeldung;
	}




	public void setFehlermeldung(String fehlermeldung) {
		this.fehlermeldung = fehlermeldung;
	}




	public String getErfolgsNachricht() {
		return erfolgsNachricht;
	}




	public void setErfolgsNachricht(String erfolgsNachricht) {
		this.erfolgsNachricht = erfolgsNachricht;
	}
	
	
	public boolean isAnonymeMeldung() {
	    return anonymeMeldung;
	}

	public void setAnonymeMeldung(boolean anonymeMeldung) {
	    this.anonymeMeldung = anonymeMeldung;
	}

	
	public String erstellungAnonym() {
	    this.anonymeMeldung = true;
	    return erstellung();
	}
    
    
    
    
}