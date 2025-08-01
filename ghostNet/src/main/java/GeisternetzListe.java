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
    
    
    
    
    // Was ist mit der Funktion warum ist die hier dopelt drinnen? 
    
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
    
  
    
    
    
    
    
    
    
    
    
    
   
    
   
    
    
    
    
    
    
    
   
    public List<Bergung> getBergungenFuerGeisternetz(Geisternetz geisternetz) {
        return bergungDAO.getBergungenByGeisternetz(geisternetz);
    }
    
   
    public int getAnzahlBergungen(Geisternetz geisternetz) {
        return getBergungenFuerGeisternetz(geisternetz).size();
    }
    
  
 
    
 
    public Person getErstenBerger(Geisternetz geisternetz) {
        return getBergungenFuerGeisternetz(geisternetz).stream()
            .filter(b -> b.getBerger() != null)
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
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
            return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
        return "Unbekannt";
    }

    public boolean istBergungGeplant(Geisternetz geisternetz) {
        List<Bergung> bergungen = getBergungenFuerGeisternetz(geisternetz);
        return bergungen.stream()
            .anyMatch(b -> b.getGeplanteBergung() != null && b.getTatsaechlicheBergung() == null);
    }
    
    
    
    
    

    private List<Geisternetz> filteredGeisternetze;

    public List<Geisternetz> getFilteredGeisternetze() {
        return filteredGeisternetze;
    }

    public void setFilteredGeisternetze(List<Geisternetz> filteredGeisternetze) {
        this.filteredGeisternetze = filteredGeisternetze;
    }

    public List<Geisternetz> getGeisternetzeWirdGeborgen() {
        return getGeisternetze().stream()
            .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
            .collect(java.util.stream.Collectors.toList());
    }

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