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
    
    @Inject
    private MeldungDAO meldungDAO;
    
    @Inject
    private BergungDAO bergungDAO;
    
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
        }
    }
    
    public NetzStatus[] getNetzStatus() {
        return NetzStatus.values();
    }
    
    // ===== ZUSÄTZLICHE METHODEN FÜR MELDEN =====
    
    public void geisternetzHinzufuegen(Geisternetz geisternetz) {
        if (geisternetz != null) {
            List<Geisternetz> geisternetze = getGeisternetze();
            geisternetze.add(geisternetz);
        }
    }
   
    public void resetNeuesGeisternetz() {
        this.neuesGeisternetz = new Geisternetz();
        this.neuesGeisternetz.setNetzStatus(NetzStatus.GEMELDET);
    }
    
    public GeisternetzDAO getDao() {
        return geisternetzDAO;
    }
    
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
    
    public String getFormatierteMeldungsDatum(Geisternetz geisternetz) {
        LocalDateTime datum = getErsteMeldungsDatum(geisternetz);
        if (datum != null) {
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "Unbekannt";
    }
    
    
    
    
    
    
    
    
    
    public void fuerBergungAnmelden(Geisternetz geisternetz, Person berger, LocalDateTime geplanteBergung, boolean anonym) {
        try {
            EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
            
            // Bergungseintrag mit geplantem Datum erstellen
            Bergung bergung = new Bergung(geisternetz, berger, geplanteBergung, anonym);
            bergungDAO.persistBergung(bergung);
            
            // Status auf BERGUNGBEVORSTEHEND setzen
            geisternetz.setNetzStatus(NetzStatus.BERGUNGBEVORSTEHEND);
            geisternetzDAO.merge(geisternetz);
            
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback falls nötig
        }
    }
    
    public void bergungAbbrechen(Geisternetz geisternetz) {
        try {
            EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
            
            List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
            bergungen.stream()
                .filter(b -> b.getTatsaechlicheBergung() == null)
                .forEach(bergungDAO::deleteBergung);
                
            geisternetz.setNetzStatus(NetzStatus.GEMELDET);
            geisternetzDAO.merge(geisternetz);
            
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void bergungAbschliessen(Geisternetz geisternetz) {
        try {
            EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
            
            List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
            Bergung aktiveBergung = bergungen.stream()
                .filter(b -> b.getTatsaechlicheBergung() == null) // Noch nicht abgeschlossen
                .findFirst()
                .orElse(null);
                
            if (aktiveBergung != null) {
                aktiveBergung.setTatsaechlicheBergung(LocalDateTime.now());
                bergungDAO.updateBergung(aktiveBergung);
                
                geisternetz.setNetzStatus(NetzStatus.GEBORGEN);
                geisternetzDAO.merge(geisternetz);
            }
            
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
   
    public List<Bergung> getBergungenFuerGeisternetz(Geisternetz geisternetz) {
        return bergungDAO.getBergungenByGeisternetz(geisternetz);
    }
    
   
    public int getAnzahlBergungen(Geisternetz geisternetz) {
        return getBergungenFuerGeisternetz(geisternetz).size();
    }
    
  
    public boolean istAnonymGeborgen(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream().allMatch(Bergung::isAnonym);
    }
    
 
    public Person getErstenBerger(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream()
        			   .filter(m -> !m.isAnonym() && m.getBerger() != null)
                       .map(Bergung::getBerger)
                       .findFirst()
                       .orElse(null);
    }
    
    
    public LocalDateTime getGeplanteBergung(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream()
            .filter(b -> b.getTatsaechlicheBergung() == null) 
            .map(Bergung::getGeplanteBergung)
            .findFirst()
            .orElse(null);
    }

    public String getFormatiertesGeplanteDatum(Geisternetz geisternetz) {
        LocalDateTime datum = getGeplanteBergung(geisternetz);
        if (datum != null) {
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "Nicht geplant";
    }

    public LocalDateTime getTatsaechlicheBergung(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream()
            .map(Bergung::getTatsaechlicheBergung)
            .filter(datum -> datum != null)
            .max(LocalDateTime::compareTo) 
            .orElse(null);
    }

    public String getFormatiertesTatsaechlichesDatum(Geisternetz geisternetz) {
        LocalDateTime datum = getTatsaechlicheBergung(geisternetz);
        if (datum != null) {
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "Noch nicht geborgen";
    }

    public boolean istBergungGeplant(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream()
            .anyMatch(b -> b.getGeplanteBergung() != null && b.getTatsaechlicheBergung() == null);
    }
    
    
    
    
    
 // Ergänzung für die GeisternetzListe Klasse

    private List<Geisternetz> filteredGeisternetze;

    public List<Geisternetz> getFilteredGeisternetze() {
        return filteredGeisternetze;
    }

    public void setFilteredGeisternetze(List<Geisternetz> filteredGeisternetze) {
        this.filteredGeisternetze = filteredGeisternetze;
    }

    // Methode zum Abrufen nur der Geisternetze mit Status WIRDGEBORGEN
    public List<Geisternetz> getGeisternetzeWirdGeborgen() {
        return getGeisternetze().stream()
            .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
            .collect(java.util.stream.Collectors.toList());
    }

    // Zusätzliche Hilfsmethoden für die Tabelle
    public long getAnzahlAktiverBergungen() {
        return getGeisternetze().stream()
            .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
            .count();
    }

    public long getAnzahlGeplanteBergungen() {
        return getGeisternetze().stream()
            .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
            .filter(this::istBergungGeplant)
            .count();
    }

    public long getAnzahlOffeneBergungen() {
        return getGeisternetze().stream()
            .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
            .filter(g -> !istBergungGeplant(g))
            .count();
    }

    
    
}