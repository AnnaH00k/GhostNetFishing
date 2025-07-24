import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Named
@ApplicationScoped
public class MeldungDAO {
    
    EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;
    
    public MeldungDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("geisternetzsammlung").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public void persistMeldung(Meldung meldung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
            entityManager.persist(meldung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public List<Meldung> getAllMeldungen() {
        CriteriaQuery<Meldung> cq = criteriaBuilder.createQuery(Meldung.class);
        cq.from(Meldung.class);
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Meldung> getMeldungenByGeisternetz(Geisternetz geisternetz) {
        CriteriaQuery<Meldung> cq = criteriaBuilder.createQuery(Meldung.class);
        Root<Meldung> root = cq.from(Meldung.class);
        cq.where(criteriaBuilder.equal(root.get("geisternetz"), geisternetz));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Meldung> getMeldungenByPerson(Person person) {
        CriteriaQuery<Meldung> cq = criteriaBuilder.createQuery(Meldung.class);
        Root<Meldung> root = cq.from(Meldung.class);
        cq.where(criteriaBuilder.equal(root.get("melder"), person));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Meldung> getAnonymeMeldungen() {
        CriteriaQuery<Meldung> cq = criteriaBuilder.createQuery(Meldung.class);
        Root<Meldung> root = cq.from(Meldung.class);
        cq.where(criteriaBuilder.isTrue(root.get("anonym")));
        return entityManager.createQuery(cq).getResultList();
    }
        
    public void updateMeldung(Meldung meldung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
            entityManager.merge(meldung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public void deleteMeldung(Meldung meldung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
        	Meldung managedMeldung = entityManager.contains(meldung) ? 
                meldung : entityManager.merge(meldung);
            entityManager.remove(managedMeldung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) {
                t.rollback();
            }
            throw e;
        }
    }
    
    public long getMeldungsCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Meldung.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
