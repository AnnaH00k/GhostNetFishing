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

    // Neue Methode zur Auswahl eines Geisternetzes basierend auf der Nummer
    public void setAktuellesGeisternetzByNummer(int geisternetzNummer) {
        List<Geisternetz> alleGeisternetze = geisternetzListe.getGeisternetze();
        this.aktuellesGeisternetz = alleGeisternetze.stream()
            .filter(g -> g.getNr() == geisternetzNummer)
            .findFirst()
            .orElse(null);
        
        if (this.aktuellesGeisternetz != null) {
            ladeBergungenZuGeisternetz(this.aktuellesGeisternetz);
        }
    }

    public void ladeBergungenZuGeisternetz(Geisternetz geisternetz) {
        this.aktuellesGeisternetz = geisternetz;
        if (geisternetz != null) {
            this.aktuelleGeisternetzBergungen = geisternetzListe.getBergungenFuerGeisternetz(geisternetz);
        }
    }

    public List<Bergung> getAktuelleGeisternetzBergungen() {
        return aktuelleGeisternetzBergungen;
    }

    public void setAktuelleGeisternetzBergungen(List<Bergung> aktuelleGeisternetzBergungen) {
        this.aktuelleGeisternetzBergungen = aktuelleGeisternetzBergungen;
    }

    public Geisternetz getAktuellesGeisternetz() {
        return aktuellesGeisternetz;
    }

    public void setAktuellesGeisternetz(Geisternetz aktuellesGeisternetz) {
        this.aktuellesGeisternetz = aktuellesGeisternetz;
    }

 // Diese Method sollte zur fuerBergungAnmelden() Methode hinzugefügt werden:
    public void fuerBergungAnmelden() {
        if (aktuellesGeisternetz != null && geplanteBergung != null && loginController.getAktuellerNutzer() != null) {
            // Prüfen ob der Nutzer ein Berger ist
            if (!"BERGEND".equals(loginController.getAktuellerNutzer().getRollenTyp().name())) {
                System.err.println("Nutzer ist kein Berger!");
                return;
            }
            
            // Prüfen ob das Geisternetz noch gemeldet ist
            if (aktuellesGeisternetz.getNetzStatus() != NetzStatus.GEMELDET) {
                System.err.println("Geisternetz kann nicht mehr für Bergung angemeldet werden. Status: " + aktuellesGeisternetz.getNetzStatus());
                return;
            }
            
            // Neue Bergung erstellen und persistieren
            Bergung bergung = new Bergung(
                aktuellesGeisternetz,
                neueBergung.isAnonym() ? null : loginController.getAktuellerNutzer(),
                geplanteBergung,
                neueBergung.isAnonym()
            );
            
            // Bergung in Datenbank speichern
            bergungDAO.persistBergung(bergung);
            
            // GeisternetzListe über die neue Bergung informieren
            geisternetzListe.fuerBergungAnmelden(
                aktuellesGeisternetz,
                loginController.getAktuellerNutzer(),
                geplanteBergung,
                neueBergung.isAnonym()
            );
            
            // Daten nach erfolgreicher Anmeldung aktualisieren
            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
            resetBergungsForm();
        }
    }

 // Verbesserte bergungAbschliessen Methode:
    public void bergungAbschliessen() {
        if (aktuellesGeisternetz != null) {
            // Prüfen ob der aktuelle Nutzer berechtigt ist
            if (!istAktuellerNutzerBerechtigt()) {
                System.err.println("Nutzer ist nicht berechtigt, diese Bergung abzuschließen!");
                return;
            }
            
            // Aktuelle Bergung finden und aktualisieren
            List<Bergung> bergungen = bergungDAO.getBergungenByGeisternetz(aktuellesGeisternetz);
            Bergung aktiveBergung = bergungen.stream()
                .filter(b -> b.istGeplant())
                .findFirst()
                .orElse(null);
                
            if (aktiveBergung != null) {
                aktiveBergung.setTatsaechlicheBergung(LocalDateTime.now());
                bergungDAO.updateBergung(aktiveBergung);
            }
            
            // GeisternetzListe informieren
            geisternetzListe.bergungAbschliessen(aktuellesGeisternetz);
            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
        }
    }

 // Verbesserte bergungAbbrechen Methode:
    public void bergungAbbrechen() {
        if (aktuellesGeisternetz != null) {
            // Prüfen ob der aktuelle Nutzer berechtigt ist
            if (!istAktuellerNutzerBerechtigt()) {
                System.err.println("Nutzer ist nicht berechtigt, diese Bergung abzubrechen!");
                return;
            }
            
            // Aktuelle Bergung finden und löschen
            List<Bergung> bergungen = bergungDAO.getBergungenByGeisternetz(aktuellesGeisternetz);
            Bergung aktiveBergung = bergungen.stream()
                .filter(b -> b.istGeplant())
                .findFirst()
                .orElse(null);
                
            if (aktiveBergung != null) {
                bergungDAO.deleteBergung(aktiveBergung);
            }
            
            // GeisternetzListe informieren
            geisternetzListe.bergungAbbrechen(aktuellesGeisternetz);
            ladeBergungenZuGeisternetz(aktuellesGeisternetz);
        }
    }
    
    // Hilfsmethode zur Berechtigung
    private boolean istAktuellerNutzerBerechtigt() {
        if (loginController.getAktuellerNutzer() == null || aktuellesGeisternetz == null) {
            return false;
        }
        
        // Prüfen ob der Nutzer ein Berger ist
        if (!"BERGEND".equals(loginController.getAktuellerNutzer().getRollenTyp().name())) {
            return false;
        }
        
        // Prüfen ob der Nutzer der angemeldete Berger ist (falls nicht anonym)
        Person aktuellerBerger = geisternetzListe.getErstenBerger(aktuellesGeisternetz);
        if (aktuellerBerger != null) {
            return aktuellerBerger.getNr() == loginController.getAktuellerNutzer().getNr();
        }
        
        // Bei anonymen Bergungen kann jeder Berger die Bergung abschließen
        return true;
    }

    
 // Zusätzliche Hilfsmethode für bessere UI-Logik:
    public boolean istAktuellerNutzerAngemeldeterBerger() {
        if (aktuellesGeisternetz == null || loginController.getAktuellerNutzer() == null) {
            return false;
        }
        
        Person aktuellerBerger = geisternetzListe.getErstenBerger(aktuellesGeisternetz);
        if (aktuellerBerger != null) {
            return aktuellerBerger.getNr() == loginController.getAktuellerNutzer().getNr();
        }
        
        // Bei anonymen Bergungen kann jeder Berger die Aktionen durchführen
        return geisternetzListe.istAnonymGeborgen(aktuellesGeisternetz);
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
    
    private void resetBergungsForm() {
        this.neueBergung = new Bergung();
        this.geplanteBergung = null;
    }

    public String getFormatiertesGeplanteDatum(Bergung bergung) {
        if (bergung == null || bergung.getGeplanteBergung() == null) {
            return "-";
        }
        return bergung.getGeplanteBergung().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getFormatiertesTatsaechlichesDatum(Bergung bergung) {
        if (bergung == null || bergung.getTatsaechlicheBergung() == null) {
            return "Noch offen";
        }
        return bergung.getTatsaechlicheBergung().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public boolean kannBergungAbgeschlossenWerden() {
        return aktuellesGeisternetz != null &&
               aktuellesGeisternetz.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND &&
               istAktuellerNutzerBerechtigt();
    }

    public boolean istBergungGeplant() {
        return aktuellesGeisternetz != null &&
               geisternetzListe.istBergungGeplant(aktuellesGeisternetz);
    }
    
    public boolean kannFuerBergungAnmelden() {
        return aktuellesGeisternetz != null &&
               aktuellesGeisternetz.getNetzStatus() == NetzStatus.GEMELDET &&
               loginController.getAktuellerNutzer() != null &&
               "BERGEND".equals(loginController.getAktuellerNutzer().getRollenTyp().name());
    }
}