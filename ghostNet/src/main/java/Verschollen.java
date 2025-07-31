import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Verschollen implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "geisternetz_nr")
    private Geisternetz geisternetz;
    
    @ManyToOne
    @JoinColumn(name = "melder_nr")
    private Person melder;
    
    private LocalDateTime meldungsDatum;

    
    
    
   
    
    // Konstruktoren
    public Verschollen() {}
    
    public Verschollen(Geisternetz geisternetz, Person melder, String grund) {
        this.geisternetz = geisternetz;
        this.melder = melder;
        this.meldungsDatum = LocalDateTime.now();
    }
    
    // Getter und Setter
    public int getId() { return id; }
    
    public Geisternetz getGeisternetz() { return geisternetz; }
    public void setGeisternetz(Geisternetz geisternetz) { this.geisternetz = geisternetz; }
    
    public Person getMelder() { return melder; }
    public void setMelder(Person melder) { this.melder = melder; }
    
    public LocalDateTime getMeldungsDatum() { return meldungsDatum; }
    public void setMeldungsDatum(LocalDateTime meldungsDatum) { this.meldungsDatum = meldungsDatum; }
    
  
    
   
    
}
