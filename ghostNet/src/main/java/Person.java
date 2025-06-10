import java.io.Serializable;
import jakarta.persistence.*;

@Entity
public class Person implements Serializable {	
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int nr;

    private String name;
    private String telefonnummer;
    private String bild;

    @Enumerated(EnumType.STRING)
    private RollenTyp rollenTyp;

    private String passwort; // Passwort sollte gehashed gespeichert werden (hier nur als Beispiel)

    public Person() {}

    public Person(String name, String telefonnummer, String bild, RollenTyp rollenTyp, String passwort) {
        this.name = name;
        this.telefonnummer = telefonnummer;
        this.bild = bild;
        this.rollenTyp = rollenTyp;
        this.passwort = passwort;
    }

    public int getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public String getBild() {
        return bild;
    }

    public RollenTyp getRollenTyp() {
        return rollenTyp;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }

    public void setRollenTyp(RollenTyp rollenTyp) {
        this.rollenTyp = rollenTyp;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
