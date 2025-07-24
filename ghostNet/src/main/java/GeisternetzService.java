import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class GeisternetzService {
    
    @Inject
    private GeisternetzDAO geisternetzDAO;
    
    @Inject
    private PersonDAO personDAO;
    
    @Inject
    private MeldungDAO meldungDAO;
    
    @Inject
    private BergungDAO bergungsDAO;
    
    @Inject
    private VerschollenDAO verschollenDAO;
    
    // Geisternetz anonym melden
    public void meldeGeisternetzAnonym(Geisternetz geisternetz) {
        Meldung meldung = new Meldung(geisternetz, null, true );
        meldungDAO.persistMeldung(meldung);
    }
    
    // Geisternetz mit Person melden
    public void meldeGeisternetz(Geisternetz geisternetz, Person melder) {
    	Meldung meldung = new Meldung(geisternetz, melder, false);
        meldungDAO.persistMeldung(meldung);
    }
    
    // Für Bergung anmelden
    public void meldeAnFuerBergung(Geisternetz geisternetz, Person berger, Date geplanteBergung, boolean anonym) {
        // Prüfen ob Person berechtigt ist (RollenTyp.BERGEND)
        if (berger.getRollenTyp() != RollenTyp.BERGEND) {
            throw new IllegalArgumentException("Person ist nicht berechtigt für Bergungsanmeldungen");
        }
        // Convert java.util.Date to LocalDateTime
        LocalDateTime geplanteBergungDateTime = geplanteBergung.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        Bergung anmeldung = new Bergung(geisternetz, berger, geplanteBergungDateTime, anonym);
        bergungsDAO.persistBergung(anmeldung);
    }
    
    // Für Bergung anmelden - Overloaded method with LocalDateTime parameter
    public void meldeAnFuerBergung(Geisternetz geisternetz, Person berger, LocalDateTime geplanteBergung, boolean anonym) {
        // Prüfen ob Person berechtigt ist (RollenTyp.BERGEND)
        if (berger.getRollenTyp() != RollenTyp.BERGEND) {
            throw new IllegalArgumentException("Person ist nicht berechtigt für Bergungsanmeldungen");
        }
        
        Bergung anmeldung = new Bergung(geisternetz, berger, geplanteBergung, anonym);
        bergungsDAO.persistBergung(anmeldung);
    }
    
    
    
    
    // Geisternetz als verschollen melden
    public void meldeGeisternetzVerschollen(Geisternetz geisternetz, Person melder, String grund) {
        // Prüfen ob Person eine Telefonnummer hat
        if (melder.getTelefonnummer() == null || melder.getTelefonnummer().trim().isEmpty()) {
            throw new IllegalArgumentException("Telefonnummer ist für Verschollen-Meldungen erforderlich");
        }
        
        Verschollen meldung = new Verschollen(geisternetz, melder, grund);
        verschollenDAO.persistVerschollenMeldung(meldung);
        
        // Geisternetz-Status auf VERSCHOLLEN setzen
        geisternetz.setNetzStatus(NetzStatus.VERSCHOLLEN);
        geisternetzDAO.merge(geisternetz);
    }
    
    // Alle Meldungen zu einem Geisternetz abrufen
    public Map<String, List<?>> getAlleMeldungenZuGeisternetz(Geisternetz geisternetz) {
        Map<String, List<?>> meldungen = new HashMap<>();
        meldungen.put("geisternetzMeldungen", meldungDAO.getMeldungenByGeisternetz(geisternetz));
        meldungen.put("bergungsAnmeldungen", bergungsDAO.getBergungenByGeisternetz(geisternetz));
        meldungen.put("verschollenMeldungen", verschollenDAO.getVerschollenMeldungenByGeisternetz(geisternetz));
        return meldungen;
    }
    
    // Alle Aktivitäten einer Person abrufen
    public Map<String, List<?>> getAlleAktivitaetenVonPerson(Person person) {
        Map<String, List<?>> aktivitaeten = new HashMap<>();
        aktivitaeten.put("geisternetzMeldungen", meldungDAO.getMeldungenByPerson(person));
        aktivitaeten.put("bergungsAnmeldungen", bergungsDAO.getBergungenByPerson(person));
        aktivitaeten.put("verschollenMeldungen", verschollenDAO.getVerschollenMeldungenByPerson(person));
        return aktivitaeten;
    }
    
   
}