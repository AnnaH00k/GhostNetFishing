import java.io.Serializable;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ViewScoped
public class PersonenController implements Serializable {

    private static final long serialVersionUID = 1L;

    private int index = 0;

    @Inject
    private PersonDAO personDAO;

    private Person person;

    public Person getPerson() {
        if (person == null) {
            person = personDAO.getPersonAtIndex(index);
        }
        return person;
    }

    public void vor() {
        System.err.println("Saving Person " + person.getName());
        EntityTransaction t = personDAO.getAndBeginTransaction();
        personDAO.merge(person);
        t.commit();

        if (index < personDAO.getPersonCount() - 1) {
            index++;
            person = personDAO.getPersonAtIndex(index);
        }
    }

    public void zurueck() {
        System.err.println("Saving Person " + person.getName());
        EntityTransaction t = personDAO.getAndBeginTransaction();
        personDAO.merge(person);
        t.commit();

        if (index > 0) {
            index--;
            person = personDAO.getPersonAtIndex(index);
        }
    }

    // Method with extensive debugging
    public void removePerson(Person personToDelete) {
        System.err.println("===== DELETE METHOD CALLED =====");
        System.err.println("Person to delete: " + (personToDelete != null ? personToDelete.getName() + " (ID: " + personToDelete.getNr() + ")" : "NULL"));
        
        if (personToDelete != null) {
            try {
                System.err.println("Before deletion - Person count: " + personDAO.getPersonCount());
                
                // Check if person exists in database
                List<Person> allPersons = personDAO.getAllPersons();
                boolean exists = allPersons.stream().anyMatch(p -> p.getNr() == personToDelete.getNr());
                System.err.println("Person exists in database: " + exists);
                
                personDAO.removePerson(personToDelete);
                
                System.err.println("After deletion - Person count: " + personDAO.getPersonCount());
                System.err.println("Person " + personToDelete.getName() + " deletion attempt completed.");
                
            } catch (Exception e) {
                System.err.println("ERROR during deletion: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("ERROR: Person to delete is NULL!");
        }
        System.err.println("===== DELETE METHOD END =====");
    }

    // Alternative method without parameters for testing
    public void testDelete() {
        System.err.println("TEST DELETE CALLED - this method has no parameters");
    }

    public int getIndex() {
        return index;
    }

    public int getMaxIndex() {
        return (int) personDAO.getPersonCount() - 1;
    }

    public List<String> getAlleBilder() {
        return personDAO.getAlleBilder();
    }
}