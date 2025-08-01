import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("bergungsController")
@SessionScoped
public class BergungsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private GeisternetzListe geisternetzListe;

    @Inject
    private BergungDAO bergungDAO;
    
    @Inject
    private LoginController loginController;

    private Bergung neueBergung = new Bergung();
    private LocalDateTime geplanteBergung;

    private List<Bergung> aktuelleGeisternetzBergungen;
    private Geisternetz aktuellesGeisternetz;

    public void setAktuellesGeisternetzByNummer(int geisternetzNummer) {
        List<Geisternetz> alleGeisternetze = geisternetzListe.getGeisternetze();
        aktuellesGeisternetz = alleGeisternetze.stream()
            .filter(g -> g.getNr() == geisternetzNummer)
            .findFirst()
            .orElse(null);
        
        if (aktuellesGeisternetz != null) {
            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
        }
    }

    public void ladeBergungenZuGeisternetz(Geisternetz geisternetz) {
        this.aktuellesGeisternetz = geisternetz;
        if (geisternetz != null) {
            this.aktuelleGeisternetzBergungen = geisternetzListe.getBergungenFuerGeisternetz(geisternetz);
        }
    }

    public void setAktuelleGeisternetzBergungen(List<Bergung> aktuelleGeisternetzBergungen) {
        this.aktuelleGeisternetzBergungen = aktuelleGeisternetzBergungen;
    }

    public void setAktuellesGeisternetz(Geisternetz aktuellesGeisternetz) {
        this.aktuellesGeisternetz = aktuellesGeisternetz;
    }

    public void fuerBergungAnmelden() {
        if (aktuellesGeisternetz != null && geplanteBergung != null && loginController.getAktuellerNutzer() != null) {
            if (!"BERGEND".equals(loginController.getAktuellerNutzer().getRollenTyp().name())) {
                System.err.println("Nutzer ist kein Berger!");
                return;
            }
            
            if (aktuellesGeisternetz.getNetzStatus() != NetzStatus.GEMELDET) {
                System.err.println("Geisternetz kann nicht mehr für Bergung angemeldet werden. Status: " + aktuellesGeisternetz.getNetzStatus());
                return;
            }
            
            Bergung bergung = new Bergung(
                aktuellesGeisternetz,
                loginController.getAktuellerNutzer(),
                geplanteBergung
            );
            
            bergungDAO.persistBergung(bergung);
            
            aktuellesGeisternetz.setNetzStatus(NetzStatus.BERGUNGBEVORSTEHEND);
            geisternetzListe.getDao().updateGeisternetz(aktuellesGeisternetz);

            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
            resetBergungsForm();
        }
    }

    public void bergungAbschliessen() {
        if (!istAktuellerNutzerBerechtigt()) return;

        Bergung geplante = findeGeplanteBergung();
        if (geplante != null) {
            geplante.setTatsaechlicheBergung(LocalDateTime.now());
            bergungDAO.updateBergung(geplante);

            aktuellesGeisternetz.setNetzStatus(NetzStatus.GEBORGEN);
            geisternetzListe.getDao().updateGeisternetz(aktuellesGeisternetz);

            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
        }
    }


    public String bergungAbbrechen() {
        if (!istAktuellerNutzerBerechtigt()) return null;

        Bergung geplante = findeGeplanteBergung();
        if (geplante != null) {
            bergungDAO.deleteBergung(geplante);

            aktuellesGeisternetz.setNetzStatus(NetzStatus.GEMELDET);
            geisternetzListe.getDao().updateGeisternetz(aktuellesGeisternetz);

            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
        }

        return "dashboard?faces-redirect=true"; 
    }


    private Bergung findeGeplanteBergung() {
        return bergungDAO.getBergungenByGeisternetz(aktuellesGeisternetz).stream()
            .filter(Bergung::istGeplant)
            .findFirst()
            .orElse(null);
    }

    
    private boolean istAktuellerNutzerBerechtigt() {
        Person nutzer = loginController.getAktuellerNutzer();
        if (nutzer == null || aktuellesGeisternetz == null) return false;
        if (!"BERGEND".equals(nutzer.getRollenTyp().name())) return false;

        Person ersterBerger = geisternetzListe.getErstenBerger(aktuellesGeisternetz);
        return ersterBerger == null || ersterBerger.getNr() == nutzer.getNr();
    }
    
    private void resetBergungsForm() {
        neueBergung = new Bergung();
        geplanteBergung = null;
    }

    public boolean istBergungGeplant() {
        return aktuellesGeisternetz != null && geisternetzListe.istBergungGeplant(aktuellesGeisternetz);
    }

    public boolean kannFuerBergungAnmelden() {
        return aktuellesGeisternetz != null &&
               aktuellesGeisternetz.getNetzStatus() == NetzStatus.GEMELDET &&
               loginController.getAktuellerNutzer() != null &&
               "BERGEND".equals(loginController.getAktuellerNutzer().getRollenTyp().name());
    }

    public boolean kannBergungAbgeschlossenWerden() {
        return aktuellesGeisternetz != null &&
               aktuellesGeisternetz.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND &&
               istAktuellerNutzerBerechtigt();
    }

    public String getFormatiertesGeplanteDatum(Bergung bergung) {
        return (bergung == null || bergung.getGeplanteBergung() == null) ? "-" :
            bergung.getGeplanteBergung().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getFormatiertesTatsaechlichesDatum(Bergung bergung) {
        return (bergung == null || bergung.getTatsaechlicheBergung() == null) ? "Noch offen" :
            bergung.getTatsaechlicheBergung().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public List<Bergung> getAktuelleGeisternetzBergungen() {
        return aktuelleGeisternetzBergungen;
    }

    public Geisternetz getAktuellesGeisternetz() {
        return aktuellesGeisternetz;
    }

    public LocalDateTime getGeplanteBergung() {
        return geplanteBergung;
    }

    public void setGeplanteBergung(LocalDateTime geplanteBergung) {
        this.geplanteBergung = geplanteBergung;
    }

    public Bergung getNeueBergung() {
        return neueBergung;
    }

    public void setNeueBergung(Bergung neueBergung) {
        this.neueBergung = neueBergung;
    }
    
    
}