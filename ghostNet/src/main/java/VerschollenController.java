import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@SessionScoped
public class VerschollenController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginController loginController;

    @Inject
    private VerschollenDAO verschollenDAO;

    @Inject
    private GeisternetzDAO geisternetzDAO;

    @Inject
    private GeisternetzMapView geisternetzMapView;

    public void verschollenMelden(Geisternetz netz) {
        Person melder = loginController.getAktuellerNutzer();

        if (melder == null || melder.getTelefonnummer() == null || melder.getTelefonnummer().isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fehler", "Bitte hinterlegen Sie eine Telefonnummer in Ihrem Profil, um eine Verschollenmeldung abzusetzen."));
            return;
        }

         if (netz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Warnung", "Kein Geisternetz ausgewählt."));
            return;
        }

        if (netz.getNetzStatus() == NetzStatus.VERSCHOLLEN) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Warnung", "Dieses Geisternetz ist bereits als verschollen gemeldet."));
            return;
        }
        if (netz.getNetzStatus() == NetzStatus.GEBORGEN) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Information", "Dieses Geisternetz wurde bereits geborgen und kann nicht als verschollen gemeldet werden."));
            return;
        }
        
        
        

        EntityTransaction transaction = geisternetzDAO.getAndBeginTransaction();
        try {
            Verschollen meldung = new Verschollen();
            meldung.setGeisternetz(netz);
            meldung.setMelder(melder);
            meldung.setMeldungsDatum(LocalDateTime.now());
            
            geisternetzDAO.persist(meldung);

            netz.setNetzStatus(NetzStatus.VERSCHOLLEN);
            geisternetzDAO.merge(netz);

            transaction.commit();

            geisternetzMapView.refreshMapModel();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Erfolg", "Geisternetz wurde erfolgreich als verschollen gemeldet."));

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            
            System.err.println("Fehler beim Melden als verschollen: " + e.getMessage());
            e.printStackTrace();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fehler", "Fehler beim Melden als verschollen: " + e.getMessage()));
        }
    }
    public void verschollenMelden() {
        Geisternetz netz = geisternetzMapView.getSelectedGeisternetz();
        verschollenMelden(netz);
    }
}