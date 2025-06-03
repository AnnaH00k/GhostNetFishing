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

    @Inject
    private PersonenListe personenListe;

    public String login() {
        for (Person p : personenListe.getNutzer()) {
            if (p.getName().equals(benutzernameEingabe)) {
                if (p.getPasswort().equals(passwortEingabe)) {
                    personenListe.setAktuelleIndex(personenListe.getNutzer().indexOf(p));
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

    // Getter & Setter
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
        return personenListe.getAktuellerNutzer();
    }
    
    
}
