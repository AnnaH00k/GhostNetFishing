import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.Arrays;
import java.util.List;

@Named
@ApplicationScoped
public class PersonDAO {

    EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;



    public PersonDAO() {
        System.err.println("Konstruktor von PersonDAO wurde aufgerufen!");

        try {
            entityManager = Persistence.createEntityManagerFactory("geisternetzsammlung").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            long count = getPersonCount();
            System.err.println("Wir haben " + count + " Personen.");

            if (count == 0) {
                System.err.println("Initialisierung der Daten.");
                EntityTransaction t = entityManager.getTransaction();
                t.begin();
                try {
                    for (Person person : baseUsers) {
                        entityManager.persist(person);
                    }
                    t.commit();
                    System.err.println("Basisdaten erfolgreich gespeichert.");
                } catch (Exception e) {
                    if (t.isActive()) {
                        t.rollback();
                    }
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    // Base users data
    static final List<Person> baseUsers = Arrays.asList(
        new Person("Liane Herbert", "0874168602",
            "https://cdn.pixabay.com/photo/2017/08/04/11/49/person-2579938_1280.jpg",
            RollenTyp.KEINE, "passwort"),
        new Person("Hugo Müller", "7250412052",
            "https://cdn.pixabay.com/photo/2018/10/29/21/46/human-3782189_1280.jpg",
            RollenTyp.MELDEND, "passwort"),
        new Person("MauMau Moe", "0000000000",
            "https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", RollenTyp.BERGEND, "passwort")
    );

    public long getPersonCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Person.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public List<Person> getAllPersons() {
        CriteriaQuery<Person> cq = criteriaBuilder.createQuery(Person.class);
        cq.from(Person.class);
        return entityManager.createQuery(cq).getResultList();
    }

    public Person getPersonAtIndex(int pos) {
        CriteriaQuery<Person> cq = criteriaBuilder.createQuery(Person.class);
        cq.from(Person.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(pos).getSingleResult();
    }

    public List<String> getAlleBilder() {
        return entityManager.createQuery("SELECT DISTINCT a.bild FROM Person a", String.class)
            .getResultList();
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
    }

    public void merge(Person person) {
        entityManager.merge(person);
    }

    public void persist(Person person) {
        entityManager.persist(person);
    }

    public void removePerson(Person person) {
        EntityTransaction t = getAndBeginTransaction();
        entityManager.remove(entityManager.contains(person) ? person : entityManager.merge(person));
        t.commit();
    }

    public static void main(String[] args) {
        PersonDAO dao = new PersonDAO();
        System.err.println("Wir haben " + dao.getPersonCount() + " Personen.");
    }
}