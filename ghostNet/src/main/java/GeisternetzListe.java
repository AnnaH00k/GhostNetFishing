import java.io.Serializable;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ApplicationScoped
public class GeisternetzListe implements Serializable {
	
    private static final long serialVersionUID = 1L;

	
    private Geisternetz neuesGeisternetz = new Geisternetz(); 
    private int aktuellerIndex = 0;
    
    @Inject
    private GeisternetzDAO geisternetzDAO;
    
    @PostConstruct
    public void init() {
    	geisternetzDAO.getGeisternetzCount();
    }
    
    public List<Geisternetz> getGeisternetze() {
        List<Geisternetz> geisternetze = geisternetzDAO.getAllGeisternetze();
        if (geisternetze == null || geisternetze.isEmpty()) {
            System.err.println("Keine Geisternetze gefunden.");
        } else {
            System.err.println("Anzahl geladener Geisternetze: " + geisternetze.size());
        }
        return geisternetze;
    }
    
    
    
    
  

    
    public List<String> getAlleBilder() {
        return geisternetzDAO.getAlleBilder();
    }


    public Geisternetz getNeuesGeisternetz() {
        return neuesGeisternetz;
    }

    public void setNeuesGeisternetz(Geisternetz neuesGeisternetz) {
        this.neuesGeisternetz = neuesGeisternetz;
    }

    public int getAktuellerIndex() {
        return aktuellerIndex;
    }

    public void setAktuellerIndex(int index) {
        this.aktuellerIndex = index;
    }

    public Geisternetz getAktuellesGeisternetz() {
        List<Geisternetz> geisternetze = getGeisternetze();
        if (aktuellerIndex >= 0 && aktuellerIndex < geisternetze.size()) {
            return geisternetze.get(aktuellerIndex);
        }
        return null;
    }

    public void geisternetzHinzufuegen() {
        try {
            EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
            geisternetzDAO.persist(neuesGeisternetz);
            t.commit();
            neuesGeisternetz = new Geisternetz();
        } catch (Exception e) {
            e.printStackTrace();
            // Hier ggf. eine Benutzerbenachrichtigung hinzufügen
        }
    }

    public NetzStatus[] getNetzStatus() {
        return NetzStatus.values();
    }
}