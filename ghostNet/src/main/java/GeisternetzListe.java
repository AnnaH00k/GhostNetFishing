import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    // Zusätzliche DAO für Meldungen
    @Inject
    private MeldungDAO meldungDAO;
    
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
            resetNeuesGeisternetz();
        } catch (Exception e) {
            e.printStackTrace();
            // Hier ggf. eine Benutzerbenachrichtigung hinzufügen
        }
    }
    
    public NetzStatus[] getNetzStatus() {
        return NetzStatus.values();
    }
    
    // ===== ZUSÄTZLICHE METHODEN =====
    
    /**
     * Fügt ein bereits erstelltes Geisternetz zur Liste hinzu
     * @param geisternetz Das hinzuzufügende Geisternetz
     */
    public void geisternetzHinzufuegen(Geisternetz geisternetz) {
        if (geisternetz != null) {
            List<Geisternetz> geisternetze = getGeisternetze();
            geisternetze.add(geisternetz);
        }
    }
    
    /**
     * Setzt das neueGeisternetz auf eine neue Instanz zurück
     * Wird nach erfolgreichem Speichern aufgerufen
     */
    public void resetNeuesGeisternetz() {
        this.neuesGeisternetz = new Geisternetz();
        // Standardwerte setzen
        this.neuesGeisternetz.setNetzStatus(NetzStatus.GEMELDET);
    }
    
    /**
     * Gibt die DAO-Instanz zurück für direkten Zugriff
     * @return GeisternetzDAO Instanz
     */
    public GeisternetzDAO getDao() {
        return geisternetzDAO;
    }
    
    /**
     * Gibt alle Meldungen für ein bestimmtes Geisternetz zurück
     * @param geisternetz Das Geisternetz
     * @return Liste der Meldungen
     */
    public List<Meldung> getMeldungenFuerGeisternetz(Geisternetz geisternetz) {
        return meldungDAO.getMeldungenByGeisternetz(geisternetz);
    }
    
    /**
     * Zählt die Anzahl der Meldungen für ein Geisternetz
     * @param geisternetz Das Geisternetz
     * @return Anzahl der Meldungen
     */
    public int getAnzahlMeldungen(Geisternetz geisternetz) {
        return getMeldungenFuerGeisternetz(geisternetz).size();
    }
    
    /**
     * Überprüft ob ein Geisternetz anonym gemeldet wurde
     * @param geisternetz Das Geisternetz
     * @return true wenn alle Meldungen anonym sind
     */
    public boolean istAnonymGemeldet(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream().allMatch(Meldung::isAnonym);
    }
    
    /**
     * Gibt den ersten Melder eines Geisternetzes zurück (falls nicht anonym)
     * @param geisternetz Das Geisternetz
     * @return Person oder null
     */
    public Person getErstenMelder(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream()
        			   .filter(m -> !m.isAnonym() && m.getMelder() != null)
                       .map(Meldung::getMelder)
                       .findFirst()
                       .orElse(null);
    }
    
    /**
     * Gibt das Meldungsdatum des ersten Melders zurück
     * @param geisternetz Das Geisternetz
     * @return LocalDateTime oder null
     */
    public LocalDateTime getErsteMeldungsDatum(Geisternetz geisternetz) {
        List<Meldung> meldungen = getMeldungenFuerGeisternetz(geisternetz);
        return meldungen.stream()
                       .map(Meldung::getMeldungsDatum)
                       .min(LocalDateTime::compareTo)
                       .orElse(null);
    }
    
    /**
     * Formatiert das Meldungsdatum für die Anzeige
     * @param geisternetz Das Geisternetz
     * @return Formatierter String
     */
    public String getFormatierteMeldungsDatum(Geisternetz geisternetz) {
        LocalDateTime datum = getErsteMeldungsDatum(geisternetz);
        if (datum != null) {
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "Unbekannt";
    }
}