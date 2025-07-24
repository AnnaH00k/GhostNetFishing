import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

@Named
@ApplicationScoped
public class VerschollenDAO {
    
    EntityManager entityManager;
    CriteriaBuilder criteriaBuilder;
    
    public VerschollenDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("geisternetzsammlung").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public void persistVerschollenMeldung(Verschollen meldung) {
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
    
    public List<Verschollen> getAllVerschollenMeldungen() {
        CriteriaQuery<Verschollen> cq = criteriaBuilder.createQuery(Verschollen.class);
        cq.from(Verschollen.class);
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Verschollen> getVerschollenMeldungenByGeisternetz(Geisternetz geisternetz) {
        CriteriaQuery<Verschollen> cq = criteriaBuilder.createQuery(Verschollen.class);
        Root<Verschollen> root = cq.from(Verschollen.class);
        cq.where(criteriaBuilder.equal(root.get("geisternetz"), geisternetz));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Verschollen> getVerschollenMeldungenByPerson(Person person) {
        CriteriaQuery<Verschollen> cq = criteriaBuilder.createQuery(Verschollen.class);
        Root<Verschollen> root = cq.from(Verschollen.class);
        cq.where(criteriaBuilder.equal(root.get("melder"), person));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public List<Verschollen> getVerschollenMeldungenByTelefonnummer(String telefonnummer) {
        CriteriaQuery<Verschollen> cq = criteriaBuilder.createQuery(Verschollen.class);
        Root<Verschollen> root = cq.from(Verschollen.class);
        cq.where(criteriaBuilder.equal(root.get("telefonnummer"), telefonnummer));
        return entityManager.createQuery(cq).getResultList();
    }
    
  
    
    public void updateVerschollenMeldung(Verschollen meldung) {
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
    
    public void deleteVerschollenMeldung(Verschollen meldung) {
        EntityTransaction t = entityManager.getTransaction();
        t.begin();
        try {
        	Verschollen managedMeldung = entityManager.contains(meldung) ? 
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
    
    public long getVerschollenMeldungsCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Verschollen.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
