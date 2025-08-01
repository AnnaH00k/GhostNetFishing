import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

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
    @JoinColumn(name = "berger_nr", nullable = false)
    private Person berger;

    private LocalDateTime geplanteBergung;
    private LocalDateTime tatsaechlicheBergung;

    public Bergung() {}

    public Bergung(Geisternetz geisternetz, Person berger, LocalDateTime geplanteBergung) {
        this.geisternetz = geisternetz;
        this.berger = berger;
        this.geplanteBergung = geplanteBergung;
    }

    public int getId() { return id; }

    public Geisternetz getGeisternetz() { return geisternetz; }
    public void setGeisternetz(Geisternetz geisternetz) { this.geisternetz = geisternetz; }

    public Person getBerger() { return berger; }
    public void setBerger(Person berger) { this.berger = berger; }

    public LocalDateTime getGeplanteBergung() { return geplanteBergung; }
    public void setGeplanteBergung(LocalDateTime geplanteBergung) { this.geplanteBergung = geplanteBergung; }

    public LocalDateTime getTatsaechlicheBergung() { return tatsaechlicheBergung; }
    public void setTatsaechlicheBergung(LocalDateTime tatsaechlicheBergung) { this.tatsaechlicheBergung = tatsaechlicheBergung; }

    public boolean istAbgeschlossen() {
        return tatsaechlicheBergung != null;
    }

    public boolean istGeplant() {
        return geplanteBergung != null && tatsaechlicheBergung == null;
    }
}
