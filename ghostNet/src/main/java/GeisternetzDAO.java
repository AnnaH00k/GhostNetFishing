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
public class GeisternetzDAO {

    EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;



    public GeisternetzDAO() {
        System.err.println("Konstruktor von GeisternetzDAO wurde aufgerufen!");

        try {
            entityManager = Persistence.createEntityManagerFactory("geisternetzsammlung").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            long count = getGeisternetzCount();
            System.err.println("Wir haben " + count + " Geisternetze.");

            if (count == 0) {
                System.err.println("Initialisierung der Daten.");
                EntityTransaction t = entityManager.getTransaction();
                t.begin();
                try {
                    for (Geisternetz geisternetz : baseGeisternetze) {
                        entityManager.persist(geisternetz);
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
    
  
    static final List<Geisternetz> baseGeisternetze = Arrays.asList(
        new Geisternetz("Nordsee", 58.00944, 3.07881,
                "300 Quadratmeter", NetzStatus.GEMELDET, "resources/images/ghost-net-underwater.jpg"),
        new Geisternetz("Nordatlantik (Bermuda-Dreieck)", 33.23400, -68.97925,
                "Pinnadelkopf-Größe", NetzStatus.GEMELDET, "https://www.leibniz-zmt.de/images/content/forschung-research/Forschungsprojekt_Research_projects/Ghostnet_Fishing_G-Schmidt.jpg"),
        new Geisternetz("Indischer Ozean", -16.37045, 78.37225,
                "Netzreste", NetzStatus.GEMELDET, "https://oliveridleyproject.org/wp-content/uploads/2021/07/Ghost-net-found-in-Maamunagau-Maldives-2019_Emma-Hedley.jpg"),
        new Geisternetz("Südchinesisches Meer", 17.06006, 114.79667,
                 "Großes Schleppnetz", NetzStatus.GEMELDET, "https://upload.wikimedia.org/wikipedia/commons/5/50/Turtle_entangled_in_marine_debris_%28ghost_net%29.jpg") 
        );
    

    public long getGeisternetzCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Geisternetz.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public List<Geisternetz> getAllGeisternetze() {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
        cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).getResultList();
    }

    public Geisternetz getGeisternetzAtIndex(int pos) {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
        cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(pos).getSingleResult();
    }

    public List<String> getAlleBilder() {
        return entityManager.createQuery("SELECT DISTINCT a.bild FROM Geisternetz a", String.class)
            .getResultList();
    }

    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
    }

    public void merge(Geisternetz geisternetz) {
        entityManager.merge(geisternetz);
    }

    public void persist(Geisternetz geisternetz) {
        entityManager.persist(geisternetz);
    }

    public void removeGeisternetz(Geisternetz geisternetz) {
        System.err.println("===== DAO REMOVE GEISTERNETZ CALLED =====");
        System.err.println("Geisternetz to remove: " +  " (ID: " + geisternetz.getNr() + ")");
        
        try {
            EntityTransaction t = getAndBeginTransaction();
            System.err.println("Transaction started");
            
            // Check if geisternetz is managed by this EntityManager
            boolean isManaged = entityManager.contains(geisternetz);
            System.err.println("Geisternetz is managed: " + isManaged);
            
            Geisternetz managedGeisternetz;
            if (isManaged) {
                managedGeisternetz = geisternetz;
            } else {
                System.err.println("Geisternetz not managed, merging first...");
                managedGeisternetz = entityManager.merge(geisternetz);
                System.err.println("Geisterneetz merged, new ID: " + managedGeisternetz.getNr());
            }
            
            System.err.println("About to remove geisternetz...");
            entityManager.remove(managedGeisternetz);
            System.err.println("Geisternetz removed from EntityManager");
            
            System.err.println("Committing transaction...");
            t.commit();
            System.err.println("Transaction committed successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR in removeGeisternetz: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to see the error in the controller
        }
        System.err.println("===== DAO REMOVE GEISTERNETZ END =====");
    }

    public static void main(String[] args) {
    	GeisternetzDAO dao = new GeisternetzDAO();
        System.err.println("Wir haben " + dao.getGeisternetzCount() + " Geisternetze.");
    }
    
    public void persist(Meldung meldung) {
        entityManager.persist(meldung);
    }

    public void merge(Meldung meldung) {
        entityManager.merge(meldung);
    }
    
    
    
    
    public void persist(Bergung bergung) {
        entityManager.persist(bergung);
    }

    public void merge(Bergung bergung) {
        entityManager.merge(bergung);
    }
    
    
    
    
    
}