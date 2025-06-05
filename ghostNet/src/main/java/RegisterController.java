import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterController implements Serializable {
    
    @Inject
    private PersonenListe personenListe;
    
    private String passwortBestaetigung;
    private String fehlermeldung;
    
    public RegisterController() {
        this.passwortBestaetigung = "";
        this.fehlermeldung = "";
    }
    
    public String registrieren() {
        // Fehlermeldung zurücksetzen
        fehlermeldung = "";
        
        // Validierung durchführen
        if (!validiereEingaben()) {
            return null; // Auf der gleichen Seite bleiben
        }
        
        try {
            // Neue Person speichern
            personenListe.personHinzufuegen();
            
            // Den neu registrierten Nutzer als aktuellen Nutzer setzen
            Person neuerNutzer = personenListe.getNutzer().get(personenListe.getNutzer().size() - 1);
            personenListe.setAktuelleIndex(personenListe.getNutzer().indexOf(neuerNutzer));
            
            // Erfolgreiche Registrierung - direkt zum Dashboard weiterleiten
            return "dashboard.xhtml?faces-redirect=true";
            
        } catch (Exception e) {
            fehlermeldung = "Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es erneut.";
            return null;
        }
    }
    
    private boolean validiereEingaben() {
        Person neuePerson = personenListe.getNeuePerson();
        boolean isValid = true;
        StringBuilder fehler = new StringBuilder();
        
        // Name validieren
        if (neuePerson.getName() == null || neuePerson.getName().trim().isEmpty()) {
            fehler.append("Name ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getName().trim().length() < 2) {
            fehler.append("Name muss mindestens 2 Zeichen lang sein. ");
            isValid = false;
        }
        
        // Rolle validieren
        if (neuePerson.getRollenTyp() == null) {
            fehler.append("Bitte wählen Sie eine Rolle aus. ");
            isValid = false;
        }
        
        // Passwort validieren
        if (neuePerson.getPasswort() == null || neuePerson.getPasswort().trim().isEmpty()) {
            fehler.append("Passwort ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getPasswort().length() < 6) {
            fehler.append("Passwort muss mindestens 6 Zeichen lang sein. ");
            isValid = false;
        }
        
        // Passwort-Bestätigung validieren
        if (passwortBestaetigung == null || passwortBestaetigung.trim().isEmpty()) {
            fehler.append("Passwort-Bestätigung ist ein Pflichtfeld. ");
            isValid = false;
        } else if (!neuePerson.getPasswort().equals(passwortBestaetigung)) {
            fehler.append("Die Passwörter stimmen nicht überein. ");
            isValid = false;
        }
        
        // Telefonnummer validieren (falls eingegeben)
        if (neuePerson.getTelefonnummer() != null && !neuePerson.getTelefonnummer().trim().isEmpty()) {
            String telefon = neuePerson.getTelefonnummer().trim();
            // Einfache Validierung: nur Zahlen, Leerzeichen, +, -, (, ) erlaubt
            if (!telefon.matches("[0-9+\\-\\s()]+")) {
                fehler.append("Telefonnummer enthält ungültige Zeichen. ");
                isValid = false;
            } else if (telefon.replaceAll("[^0-9]", "").length() < 6) {
                fehler.append("Telefonnummer muss mindestens 6 Ziffern enthalten. ");
                isValid = false;
            }
        }
        
        // Bild URL validieren (falls eingegeben)
        if (neuePerson.getBild() != null && !neuePerson.getBild().trim().isEmpty()) {
            String bildUrl = neuePerson.getBild().trim();
            // Einfache URL-Validierung
            if (!bildUrl.matches("^(https?://|resources/).*\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                fehler.append("Bild URL muss eine gültige URL oder ein lokaler Pfad zu einer Bilddatei sein. ");
                isValid = false;
            }
        }
        
        // Prüfen ob Name bereits existiert (nur wenn andere Validierungen erfolgreich waren)
        if (isValid && neuePerson.getName() != null && !neuePerson.getName().trim().isEmpty()) {
            if (nameExistiertBereits(neuePerson.getName().trim())) {
                fehler.append("Ein Nutzer mit diesem Namen existiert bereits. ");
                isValid = false;
            }
        }
        
        // Fehlermeldung setzen
        if (!isValid) {
            fehlermeldung = fehler.toString().trim();
        }
        
        return isValid;
    }
    
    private boolean nameExistiertBereits(String name) {
        return personenListe.getNutzer().stream()
            .anyMatch(person -> person.getName().equals(name));
    }
    
    // Getter und Setter
    public String getPasswortBestaetigung() {
        return passwortBestaetigung;
    }
    
    public void setPasswortBestaetigung(String passwortBestaetigung) {
        this.passwortBestaetigung = passwortBestaetigung;
    }
    
    public String getFehlermeldung() {
        return fehlermeldung;
    }
    
    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }
}