import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class LoginController implements Serializable {
    private static final long serialVersionUID = 1L;

    private String benutzernameEingabe;
    private String passwortEingabe;
    private String fehlermeldung;
    
    private Person aktuellerNutzer;
    private boolean eingeloggt = false;

    @Inject
    private PersonenListe personenListe;

    public String login() {
        for (Person p : personenListe.getPersonen()) {
            if (p.getName().equals(benutzernameEingabe)) {
                if (p.getPasswort().equals(passwortEingabe)) {
                	
                	 this.aktuellerNutzer = p;
                     this.eingeloggt = true;
                     
                     this.benutzernameEingabe = "";
                     this.passwortEingabe = "";
                     this.fehlermeldung = "";
                     
                    return "dashboard.xhtml?faces-redirect=true";
                } else {
                    fehlermeldung = "Falsches Passwort.";
                    return null;
                }
            }
        }
        fehlermeldung = "Benutzername nicht gefunden.";
        return null;
    }
    
    public String logout() {
        this.aktuellerNutzer = null;
        this.eingeloggt = false;
        this.benutzernameEingabe = "";
        this.passwortEingabe = "";
        this.fehlermeldung = "";
        
        
        return "landingpage.xhtml?faces-redirect=true";
    }
    
    

    public String getBenutzernameEingabe() {
        return benutzernameEingabe;
    }

    public void setBenutzernameEingabe(String benutzernameEingabe) {
        this.benutzernameEingabe = benutzernameEingabe;
    }

    public String getPasswortEingabe() {
        return passwortEingabe;
    }

    public void setPasswortEingabe(String passwortEingabe) {
        this.passwortEingabe = passwortEingabe;
    }

    public String getFehlermeldung() {
        return fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    public Person getAktuellerNutzer() {
        return aktuellerNutzer;
    }
    

	
	 public void setAktuellerNutzer(Person aktuellerNutzer) {
	     this.aktuellerNutzer = aktuellerNutzer;
	 }
	
	 public void setEingeloggt(boolean eingeloggt) {
	     this.eingeloggt = eingeloggt;
	 }
	 
 
 
    public boolean isEingeloggt() {
        return eingeloggt;
    }
    
    public boolean isNichtEingeloggt() {
        return !eingeloggt;
    }
}
