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

    
    private String telefonnummer; // Redundant, aber für Sicherheit
    
    private String grund; // Grund für Verschollen-Meldung
    
   
    
    // Konstruktoren
    public Verschollen() {}
    
    public Verschollen(Geisternetz geisternetz, Person melder, String grund) {
        this.geisternetz = geisternetz;
        this.melder = melder;
        this.telefonnummer = melder.getTelefonnummer();
        this.grund = grund;
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
    
    public String getTelefonnummer() { return telefonnummer; }
    public void setTelefonnummer(String telefonnummer) { this.telefonnummer = telefonnummer; }
    
    public String getGrund() { return grund; }
    public void setGrund(String grund) { this.grund = grund; }
    
}
