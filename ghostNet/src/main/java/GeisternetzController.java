import java.io.Serializable;
import java.util.List;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ViewScoped
public class GeisternetzController implements Serializable {

    private static final long serialVersionUID = 1L;

    private int index = 0;

    @Inject
    private GeisternetzDAO geisternetzDAO;

    private Geisternetz geisternetz;

    public Geisternetz getGeisternetz() {
        if (geisternetz == null) {
        	geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
        return geisternetz;
    }

    public void vor() {
        System.err.println("Saving Geisternetz " + geisternetz.getNr());
        EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
        geisternetzDAO.merge(geisternetz);
        t.commit();

        if (index < geisternetzDAO.getGeisternetzCount() - 1) {
            index++;
            geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
    }

    public void zurueck() {
        System.err.println("Saving Geisternetz " + geisternetz.getNr());
        EntityTransaction t = geisternetzDAO.getAndBeginTransaction();
        geisternetzDAO.merge(geisternetz);
        t.commit();

        if (index > 0) {
            index--;
            geisternetz = geisternetzDAO.getGeisternetzAtIndex(index);
        }
    }

    // Method with extensive debugging
    public void removeGeisternetz(Geisternetz geisternetzToDelete) {
        System.err.println("===== DELETE METHOD CALLED =====");
        System.err.println("Person to delete: " + (geisternetzToDelete != null ?  " (ID: " + geisternetzToDelete.getNr() + ")" : "NULL"));
        
        if (geisternetzToDelete != null) {
            try {
                System.err.println("Before deletion - Person count: " + geisternetzDAO.getGeisternetzCount());
                
                // Check if person exists in database
                List<Geisternetz> alleGeisternetze = geisternetzDAO.getAllGeisternetze();
                boolean exists = alleGeisternetze.stream().anyMatch(p -> p.getNr() == geisternetzToDelete.getNr());
                System.err.println("Geisternetz exists in database: " + exists);
                
                geisternetzDAO.removeGeisternetz(geisternetzToDelete);
                
                System.err.println("After deletion - Geisternetz count: " + geisternetzDAO.getGeisternetzCount());
                System.err.println("Geisternetz " + geisternetzToDelete.getNr() + " deletion attempt completed.");
                
            } catch (Exception e) {
                System.err.println("ERROR during deletion: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("ERROR: Geisternetz to delete is NULL!");
        }
        System.err.println("===== GEISTERNETZ METHOD END =====");
    }

    // Alternative method without parameters for testing
    public void testDelete() {
        System.err.println("TEST DELETE CALLED - this method has no parameters");
    }

    public int getIndex() {
        return index;
    }

    public int getMaxIndex() {
        return (int) geisternetzDAO.getGeisternetzCount() - 1;
    }

    public List<String> getAlleBilder() {
        return geisternetzDAO.getAlleBilder();
    }
}