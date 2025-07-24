import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Meldung implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "geisternetz_nr")
    private Geisternetz geisternetz;
    
    @ManyToOne
    @JoinColumn(name = "melder_nr", nullable = true) 
    private Person melder;
    
    
   private LocalDateTime meldungsDatum;
    
    private boolean anonym;
    
    
   
    
    public Meldung() {}
    
    public Meldung(Geisternetz geisternetz, Person melder, boolean anonym) {
        this.geisternetz = geisternetz;
        this.melder = anonym ? null : melder;
        this.anonym = anonym;
        this.meldungsDatum = LocalDateTime.now();
    }

public int getId() { return id; }
    
    public Geisternetz getGeisternetz() { return geisternetz; }
    public void setGeisternetz(Geisternetz geisternetz) { this.geisternetz = geisternetz; }
    
    public Person getMelder() { return melder; }
    public void setMelder(Person melder) { this.melder = melder; }
    
    public LocalDateTime getMeldungsDatum() { return meldungsDatum; }
    public void setMeldungsDatum(LocalDateTime meldungsDatum) { this.meldungsDatum = meldungsDatum; }
    
    public boolean isAnonym() { return anonym; }
    public void setAnonym(boolean anonym) { this.anonym = anonym; }
    
    
}
