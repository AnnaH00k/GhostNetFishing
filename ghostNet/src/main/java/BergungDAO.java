import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Named
@ApplicationScoped
public class BergungDAO {
    
    EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;
    
    public BergungDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("geisternetzsammlung").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public void persistBergung(Bergung bergung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
            entityManager.persist(bergung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public List<Bergung> getAllBergungen() {
        CriteriaQuery<Bergung> cq = criteriaBuilder.createQuery(Bergung.class);
        cq.from(Bergung.class);
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Bergung> getBergungenByGeisternetz(Geisternetz geisternetz) {
        CriteriaQuery<Bergung> cq = criteriaBuilder.createQuery(Bergung.class);
        Root<Bergung> root = cq.from(Bergung.class);
        cq.where(criteriaBuilder.equal(root.get("geisternetz"), geisternetz));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Bergung> getBergungenByPerson(Person person) {
        CriteriaQuery<Bergung> cq = criteriaBuilder.createQuery(Bergung.class);
        Root<Bergung> root = cq.from(Bergung.class);
        cq.where(criteriaBuilder.equal(root.get("berger"), person));
        return entityManager.createQuery(cq).getResultList();
    }
    
        
    public void updateBergung(Bergung bergung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
            entityManager.merge(bergung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public void deleteBergung(Bergung bergung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
        	Bergung managedBergung = entityManager.contains(bergung) ? 
        			bergung : entityManager.merge(bergung);
            entityManager.remove(managedBergung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public long getBergungsCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Bergung.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}