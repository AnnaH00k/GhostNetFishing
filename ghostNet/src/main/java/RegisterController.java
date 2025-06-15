import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterController implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private PersonenListe personenListe;
    
    @Inject
    private LoginController loginController;
    
    private String passwortBestaetigung;
    private String fehlermeldung;
    
    private static final List<String> DEFAULT_IMAGES = Arrays.asList(
        "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUSEhIVFhUVFRUWFRUVFRUVFxYVFRcWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGhAQGi0fHyUtLS0tLS0tLS0tLSstLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAMEBBQMBIgACEQEDEQH/xAAbAAABBQEBAAAAAAAAAAAAAAADAAECBAUGB...",
        "https://t4.ftcdn.net/jpg/02/53/61/69/360_F_253616948_za22DUrpvoM6aBDyPZxXDXf1OVNZFhL4.jpg",
        "https://www.aqueon.com/-/media/project/oneweb/aqueon/us/blog/top-5-aggressive-fish-tank-species-for-your-home-aquarium/shutterstock-2175454025-web.jpg",
        "https://images.theconversation.com/files/299379/original/file-20191030-154716-1wc4d64.jpg?ixlib=rb-4.1.0&rect=0%2C12%2C2048%2C1023&q=45&auto=format&w=1356&h=668&fit=crop",
        "https://www.marlinmag.com/wp-content/uploads/2023/10/black-marlin-hooked-panama-tropic-star-lodge-1024x768.jpg",
        "https://cdn.prod.www.spiegel.de/images/b237b5e1-2d68-41d9-b233-4e7ff03514d5_w960_r1.778_fpx48_fpy49.jpg"
    );
    
    private Random random = new Random();
    
    public RegisterController() {
        this.passwortBestaetigung = "";
        this.fehlermeldung = "";
    }
    
    public String registrieren() {
        fehlermeldung = "";
        
        if (!validiereEingaben()) {
            return null; // Stay on the same page
        }
        
        try {
            // Add the new person to the database
            personenListe.personHinzufuegen();
            
            // Get the newly created person (last one added)
            List<Person> personen = personenListe.getPersonen();
            Person neuerNutzer = personen.get(personen.size() - 1);
            
            // Automatically log in the newly registered user
            loginController.setAktuellerNutzer(neuerNutzer);
            loginController.setEingeloggt(true);
            
            // Clear form fields
            clearForm();
            
            return "dashboard.xhtml?faces-redirect=true";
            
        } catch (Exception e) {
            fehlermeldung = "Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es erneut.";
            e.printStackTrace(); // For debugging
            return null;
        }
    }
    
    private void clearForm() {
        this.passwortBestaetigung = "";
        this.fehlermeldung = "";
        // Reset the neuePerson object in PersonenListe
        personenListe.setNeuePerson(new Person());
    }
   
    private String getRandomImage() {
        return DEFAULT_IMAGES.get(random.nextInt(DEFAULT_IMAGES.size()));
    }
    
    private boolean validiereEingaben() {
        Person neuePerson = personenListe.getNeuePerson();
        boolean isValid = true;
        StringBuilder fehler = new StringBuilder();
        
        // Validate name
        if (neuePerson.getName() == null || neuePerson.getName().trim().isEmpty()) {
            fehler.append("Name ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getName().trim().length() < 2) {
            fehler.append("Name muss mindestens 2 Zeichen lang sein. ");
            isValid = false;
        }
        
        // Validate role
        if (neuePerson.getRollenTyp() == null) {
            fehler.append("Bitte wählen Sie eine Rolle aus. ");
            isValid = false;
        }
        
        // Validate password
        if (neuePerson.getPasswort() == null || neuePerson.getPasswort().trim().isEmpty()) {
            fehler.append("Passwort ist ein Pflichtfeld. ");
            isValid = false;
        } else if (neuePerson.getPasswort().length() < 6) {
            fehler.append("Passwort muss mindestens 6 Zeichen lang sein. ");
            isValid = false;
        }
        
        // Validate password confirmation
        if (passwortBestaetigung == null || passwortBestaetigung.trim().isEmpty()) {
            fehler.append("Passwort-Bestätigung ist ein Pflichtfeld. ");
            isValid = false;
        } else if (!neuePerson.getPasswort().equals(passwortBestaetigung)) {
            fehler.append("Die Passwörter stimmen nicht überein. ");
            isValid = false;
        }
        
        // Validate phone number (optional)
        if (neuePerson.getTelefonnummer() != null && !neuePerson.getTelefonnummer().trim().isEmpty()) {
            String telefon = neuePerson.getTelefonnummer().trim();
            if (!telefon.matches("[0-9+\\-\\s()]+")) {
                fehler.append("Telefonnummer enthält ungültige Zeichen. ");
                isValid = false;
            } else if (telefon.replaceAll("[^0-9]", "").length() < 6) {
                fehler.append("Telefonnummer muss mindestens 6 Ziffern enthalten. ");
                isValid = false;
            }
        }
        
        // Set random image if none provided
        if (neuePerson.getBild() == null || neuePerson.getBild().trim().isEmpty()) {
            String randomImage = getRandomImage();
            neuePerson.setBild(randomImage);
        }
        
        // Check if name already exists
        if (isValid && neuePerson.getName() != null && !neuePerson.getName().trim().isEmpty()) {
            if (nameExistiertBereits(neuePerson.getName().trim())) {
                fehler.append("Ein Nutzer mit diesem Namen existiert bereits. ");
                isValid = false;
            }
        }
        
        if (!isValid) {
            fehlermeldung = fehler.toString().trim();
        }
        
        return isValid;
    }
    
    private boolean nameExistiertBereits(String name) {
        return personenListe.getPersonen().stream()
            .anyMatch(person -> person.getName().equals(name));
    }
    
    // Getters and Setters
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
    
    public List<String> getDefaultImages() {
        return DEFAULT_IMAGES;
    }
}