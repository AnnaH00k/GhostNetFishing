import java.io.Serializable;
import jakarta.persistence.*;

@Entity
public class Person implements Serializable {	
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nr;

    private String name;
    private String telefonnummer;
    private String bild;
    private String passwort; // Passwort sollte gehashed gespeichert werden (hier nur als Beispiel)
    @Enumerated(EnumType.STRING)
    private RollenTyp rollenTyp;


    public Person() {}

    public Person(String name, String telefonnummer, String bild,String passwort, RollenTyp rollenTyp ) {
        this.name = name;
        this.telefonnummer = telefonnummer;
        this.bild = bild;
        this.passwort = passwort;
        this.rollenTyp = rollenTyp;
    }
    
    
    

    public int getNr() {
        return nr;
    }
    
  
    
    
    

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
    
    public String getTelefonnummer() {
        return telefonnummer;
    }
    
    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    
    
    
    
    
    public String getBild() {
        return bild;
    } 
    
    public void setBild(String bild) {
        this.bild = bild;
    }
    
    
    
    
    
    

    public String getPasswort() {
        return passwort;
    }  
    
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
    
    
    
    
    
    

    public RollenTyp getRollenTyp() {
        return rollenTyp;
    }

    public void setRollenTyp(RollenTyp rollenTyp) {
        this.rollenTyp = rollenTyp;
    }
    

  
}
