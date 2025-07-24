import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Bergung implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "geisternetz_nr")
    private Geisternetz geisternetz;
    
    @ManyToOne
    @JoinColumn(name = "berger_nr", nullable = true) 
    private Person berger;
    
    
	private LocalDateTime geplanteBergung;
	    
	private LocalDateTime tatsaechlicheBergung;
    
    
    private boolean anonym;
    
    
   
    
    public Bergung() {}
    
    public Bergung(Geisternetz geisternetz, Person berger, LocalDateTime geplanteBergung, boolean anonym) {
        this.geisternetz = geisternetz;
        this.berger = anonym ? null : berger;
        this.geplanteBergung = geplanteBergung;
        this.anonym = anonym;
    }

public int getId() { return id; }
    
    public Geisternetz getGeisternetz() { return geisternetz; }
    public void setGeisternetz(Geisternetz geisternetz) { this.geisternetz = geisternetz; }
    
    public Person getBerger() { return berger; }
    public void setBerger(Person berger) { this.berger = berger; }
    
    public boolean isAnonym() { return anonym; }
    public void setAnonym(boolean anonym) { this.anonym = anonym; }
    
    public LocalDateTime getGeplanteBergung() { 
        return geplanteBergung; 
    }
    
    public void setGeplanteBergung(LocalDateTime geplanteBergung) { 
        this.geplanteBergung = geplanteBergung; 
    }

    public LocalDateTime getTatsaechlicheBergung() { 
        return tatsaechlicheBergung; 
    }
    
    public void setTatsaechlicheBergung(LocalDateTime tatsaechlicheBergung) { 
        this.tatsaechlicheBergung = tatsaechlicheBergung; 
    }
    public boolean istAbgeschlossen() {
        return tatsaechlicheBergung != null;
    }

    public boolean istGeplant() {
        return geplanteBergung != null && tatsaechlicheBergung == null;
    }
    
    
}

