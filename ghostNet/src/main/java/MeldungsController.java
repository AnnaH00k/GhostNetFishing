import java.io.Serializable;
import java.time.LocalDateTime;
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
    private MeldungDAO meldungDAO;
    @Inject
    private GeisternetzDAO geisternetzDAO;
    @Inject
    private GeisternetzListe geisternetzListe;
    
    private List<Meldung> aktuelleGeisternetzMeldungen;
    private Geisternetz aktuellesGeisternetz;
        
    public List<Meldung> getMeldungenFuerGeisternetz(Geisternetz geisternetz) {
        return meldungDAO.getMeldungenByGeisternetz(geisternetz);
    }
    
    public int getAnzahlMeldungen(Geisternetz geisternetz) {
        return getMeldungenFuerGeisternetz(geisternetz).size();
    }
    
    public boolean istAnonymGemeldet(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream().allMatch(Meldung::isAnonym);
    }
    
    public Person getErstenMelder(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream()
                       .filter(m -> !m.isAnonym() && m.getMelder() != null)
                       .map(Meldung::getMelder)
                       .findFirst()
                       .orElse(null);
    }
    
    public LocalDateTime getErsteMeldungsDatum(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream()
                       .map(Meldung::getMeldungsDatum)
                       .min(LocalDateTime::compareTo)
                       .orElse(null);
    }
    
 
    public String getFormatiertesMeldungsDatum(Geisternetz geisternetz) {
        LocalDateTime datum = getErsteMeldungsDatum(geisternetz);
        if (datum != null) {
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "Unbekannt";
    }
    
    


    
    public void ladeMeldungenZuGeisternetz(Geisternetz geisternetz) {
        this.aktuellesGeisternetz = geisternetz;
        this.aktuelleGeisternetzMeldungen = getMeldungenFuerGeisternetz(geisternetz);
    }
    
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
    
    
    
}