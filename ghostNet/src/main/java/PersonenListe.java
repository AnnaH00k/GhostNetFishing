import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;
import java.util.List;

@Named
@ApplicationScoped
public class PersonenListe implements Serializable {
    private static final long serialVersionUID = 1L;

    private Person neuePerson = new Person();

    @Inject
    private PersonDAO personDAO;

    @PostConstruct
    public void init() {
        personDAO.getPersonCount();
    }

    public List<Person> getPersonen() {
        List<Person> personen = personDAO.getAllPersons();
        if (personen == null || personen.isEmpty()) {
            System.err.println("Keine Personen gefunden.");
        } else {
            System.err.println("Anzahl geladener Personen: " + personen.size());
        }
        return personen;
    }
    
    
    
    public List<String> getAlleBilder() {
        return personDAO.getAlleBilder();
    }


    public Person getNeuePerson() {
        return neuePerson;
    }

    public void setNeuePerson(Person neuePerson) {
        this.neuePerson = neuePerson;
    }


    public void personHinzufuegen() {
        try {
            EntityTransaction t = personDAO.getAndBeginTransaction();
            personDAO.persist(neuePerson);
            t.commit();
            neuePerson = new Person();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RollenTyp[] getRollenTypen() {
        return RollenTyp.values();
    }
}