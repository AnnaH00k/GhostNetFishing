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

    public void removePerson() {
        if (personDAO.getPersonCount() > 0 && person != null) {
            personDAO.removePerson(person);
            if (index > 0) index--;
            person = personDAO.getPersonAtIndex(index);
        }
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