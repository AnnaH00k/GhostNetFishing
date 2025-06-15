import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    
    
    
    @OneToMany(mappedBy = "melder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meldung> geisternetzMeldungen = new ArrayList<>();
    
    @OneToMany(mappedBy = "berger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bergung> bergungsAnmeldungen = new ArrayList<>();
    
    @OneToMany(mappedBy = "melder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Verschollen> verschollenMeldungen = new ArrayList<>();
    
    
    


    public Person() {}

    public Person(String name, String telefonnummer, String bild,String passwort, RollenTyp rollenTyp ) {
        this.name = name;
        this.telefonnummer = telefonnummer;
        this.bild = bild;
        this.passwort = passwort;
        this.rollenTyp = rollenTyp;
    }
    
    
    
    
    public List<Meldung> getGeisternetzMeldungen() { return geisternetzMeldungen; }
    public void setGeisternetzMeldungen(List<Meldung> geisternetzMeldungen) { this.geisternetzMeldungen = geisternetzMeldungen; }
    
    public List<Bergung> getBergungsAnmeldungen() { return bergungsAnmeldungen; }
    public void setBergungsAnmeldungen(List<Bergung> bergungsAnmeldungen) { this.bergungsAnmeldungen = bergungsAnmeldungen; }
    
    public List<Verschollen> getVerschollenMeldungen() { return verschollenMeldungen; }
    public void setVerschollenMeldungen(List<Verschollen> verschollenMeldungen) { this.verschollenMeldungen = verschollenMeldungen; }
    
    
    
    
    
    

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
