import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("meldungsController")
@SessionScoped
public class MeldungsController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private GeisternetzListe geisternetzListe;
    
    private List<Meldung> aktuelleGeisternetzMeldungen;
    private Geisternetz aktuellesGeisternetz;
    
    /**
     * Lädt alle Meldungen für ein bestimmtes Geisternetz
     * @param geisternetz Das Geisternetz
     */
    public void ladeMeldungenZuGeisternetz(Geisternetz geisternetz) {
        this.aktuellesGeisternetz = geisternetz;
        this.aktuelleGeisternetzMeldungen = geisternetzListe.getMeldungenFuerGeisternetz(geisternetz);
    }
    
    // Getter und Setter
    public List<Meldung> getAktuelleGeisternetzMeldungen() {
        return aktuelleGeisternetzMeldungen;
    }
    
    public void setAktuelleGeisternetzMeldungen(List<Meldung> aktuelleGeisternetzMeldungen) {
        this.aktuelleGeisternetzMeldungen = aktuelleGeisternetzMeldungen;
    }
    
    public Geisternetz getAktuellesGeisternetz() {
        return aktuellesGeisternetz;
    }
    
    public void setAktuellesGeisternetz(Geisternetz aktuellesGeisternetz) {
        this.aktuellesGeisternetz = aktuellesGeisternetz;
    }
    
    public String getFormatiertesDatum(Meldung meldung) {
        if (meldung.getMeldungsDatum() == null) {
            return "-";
        }
        return meldung.getMeldungsDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}