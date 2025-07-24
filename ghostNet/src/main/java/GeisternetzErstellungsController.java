import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@RequestScoped
public class GeisternetzErstellungsController implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private GeisternetzListe geisternetzListe;
    
    @Inject
    private MeldungDAO meldungDAO;
    
    @Inject
    private LoginController loginController;

    private String fehlermeldung;
    private boolean anonymeMeldung = false;
    private String erfolgsNachricht;

    // ===== Automatische Bildvergabe =====
    private static final List<String> DEFAULT_IMAGES = Arrays.asList(
        "https://www.shutterstock.com/image-vector/vector-illustration-fishnet-fishing-symbol-260nw-1464441224.jpg",
        "https://cdn-icons-png.flaticon.com/512/2083/2083845.png",
        "https://www.shutterstock.com/image-vector/doodle-fish-net-vector-hand-260nw-2136940005.jpg"
    );
    private Random random = new Random();

    private String getRandomImage() {
        return DEFAULT_IMAGES.get(random.nextInt(DEFAULT_IMAGES.size()));
    }

    // ===== Meldung erstellen und speichern =====
    public String erstellung() {
        try {
            fehlermeldung = null;
            erfolgsNachricht = null;

            if (!validiereEingaben()) {
                return null;
            }

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

                erfolgsNachricht = anonymeMeldung
                    ? "Geisternetz wurde erfolgreich anonym gemeldet! Vielen Dank für Ihren Beitrag zum Meeresschutz."
                    : "Geisternetz wurde erfolgreich gemeldet! Vielen Dank für Ihren Beitrag zum Meeresschutz.";

                geisternetzListe.resetNeuesGeisternetz();
                anonymeMeldung = false;

                return "dankeseite.xhtml?faces-redirect=true";

            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            fehlermeldung = "Fehler beim Speichern der Meldung: " + e.getMessage();
            return null;
        }
    }

    // ===== Validierung inkl. automatischer Bildvergabe =====
    private boolean validiereEingaben() {
        Geisternetz geisternetz = geisternetzListe.getNeuesGeisternetz();

        if (geisternetz.getGroeße() == null || geisternetz.getGroeße().trim().isEmpty()) {
            fehlermeldung = "Bitte geben Sie eine Netzgröße an.";
            return false;
        }

        if (geisternetz.getNetzStatus() == null) {
            fehlermeldung = "Bitte wählen Sie einen Netzstatus aus.";
            return false;
        }

        if (geisternetz.getLat() < -90 || geisternetz.getLat() > 90) {
            fehlermeldung = "Breitengrad muss zwischen -90 und 90 liegen.";
            return false;
        }

        if (geisternetz.getLng() < -180 || geisternetz.getLng() > 180) {
            fehlermeldung = "Längengrad muss zwischen -180 und 180 liegen.";
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

    // ===== Hilfsmethoden =====
    private Person getCurrentUser() {
        return loginController.getAktuellerNutzer();
    }

    public long getAnzahlAnonymerMeldungen() {
        return meldungDAO.getAnonymeMeldungen().size();
    }

    public boolean istBenutzerEingeloggt() {
        return getCurrentUser() != null;
    }

    public String getAnonymitaetsHinweis() {
        if (anonymeMeldung) {
            return "Ihre Meldung wird anonym gespeichert. Es werden keine persönlichen Daten erfasst.";
        } else if (istBenutzerEingeloggt()) {
            return "Ihre Meldung wird mit Ihrem Benutzerkonto verknüpft.";
        } else {
            return "Da Sie nicht eingeloggt sind, wird Ihre Meldung automatisch als anonym behandelt.";
        }
    }

    // ===== Getter & Setter =====
    public String getFehlermeldung() {
        return fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    public boolean isAnonymeMeldung() {
        return anonymeMeldung;
    }

    public void setAnonymeMeldung(boolean anonymeMeldung) {
        this.anonymeMeldung = anonymeMeldung;
    }

    public String getErfolgsNachricht() {
        return erfolgsNachricht;
    }

    public void setErfolgsNachricht(String erfolgsNachricht) {
        this.erfolgsNachricht = erfolgsNachricht;
    }

    public List<String> getDefaultImages() {
        return DEFAULT_IMAGES;
    }
}
