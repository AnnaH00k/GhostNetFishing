import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

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
    
    @Inject
    private PersonDAO personDAO;  
    

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
    
    
    public void profilSpeichern() { 
        try {
            EntityTransaction t = personDAO.getAndBeginTransaction();
            personDAO.merge(aktuellerNutzer);
            t.commit();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Profil erfolgreich gespeichert!"));
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Speichern!", null));
        }
    }
    
    
    
    public String werdeMeldendePerson() {
        try {
            aktuellerNutzer.setRollenTyp(RollenTyp.MELDEND);
            EntityTransaction t = personDAO.getAndBeginTransaction();
            personDAO.merge(aktuellerNutzer);
            t.commit();

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Rolle erfolgreich gesetzt: MELDEND"));

            return "dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Setzen der Rolle!", null));
            return null;
        }
    }

    
    public String werdeBergendePerson() {
        try {
            aktuellerNutzer.setRollenTyp(RollenTyp.BERGEND);
            EntityTransaction t = personDAO.getAndBeginTransaction();
            personDAO.merge(aktuellerNutzer);
            t.commit();

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Rolle erfolgreich gesetzt: BERGEND"));

            return "dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Setzen der Rolle!", null));
            return null;
        }
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
