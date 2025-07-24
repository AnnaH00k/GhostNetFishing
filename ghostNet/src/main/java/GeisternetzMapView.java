import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@Named
@ViewScoped
public class GeisternetzMapView implements Serializable {

    private MapModel geisternetzModel;
    private Marker selectedMarker;

    @Inject
    private GeisternetzListe geisternetzListe;
    
    @Inject
    private BergungsController bergungsController;

    @PostConstruct
    public void init() {
        geisternetzModel = new DefaultMapModel();

        for (Geisternetz netz : geisternetzListe.getGeisternetze()) {
            LatLng koordinaten = new LatLng(netz.getLat(), netz.getLng());

            // Statusabhängige Darstellung
            String statusColor = getStatusColor(netz.getNetzStatus());
            String statusIcon = getStatusIcon(netz.getNetzStatus());
            
            String info = createInfoString(netz);
            
            // Titel enthält die Geisternetz-Nummer für einfache Identifikation
            Marker marker = new Marker(koordinaten, String.valueOf(netz.getNr()), info);
            marker.setIcon("http://maps.google.com/mapfiles/ms/icons/" + statusColor + "-dot.png");
            
            geisternetzModel.addOverlay(marker);
        }
    }
    
    private String createInfoString(Geisternetz netz) {
        StringBuilder info = new StringBuilder();
        info.append("<div style='max-width: 300px;'>");
        info.append("<b>Nr:</b> ").append(netz.getNr()).append("<br>");
        info.append("<b>Größe:</b> ").append(netz.getGroeße()).append("<br>");
        info.append("<b>Status:</b> <span style='color: ").append(getStatusTextColor(netz.getNetzStatus())).append(";'>");
        info.append(getStatusDisplayName(netz.getNetzStatus())).append("</span><br>");
        info.append("<b>Standort:</b> ").append(netz.getStandort()).append("<br>");
        info.append("<b>Koordinaten:</b> ").append(netz.getLat()).append(", ").append(netz.getLng()).append("<br>");
        
        // Meldungsinformationen
        info.append("<b>Gemeldet am:</b> ").append(geisternetzListe.getFormatierteMeldungsDatum(netz)).append("<br>");
        
        // Bergungsinformationen je nach Status
        if (netz.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND) {
            info.append("<b>Geplante Bergung:</b> ").append(geisternetzListe.getFormatiertesGeplanteDatum(netz)).append("<br>");
            if (!geisternetzListe.istAnonymGeborgen(netz)) {
                Person berger = geisternetzListe.getErstenBerger(netz);
                if (berger != null) {
                    info.append("<b>Berger:</b> ").append(berger.getName()).append("<br>");
                }
            } else {
                info.append("<b>Berger:</b> Anonym<br>");
            }
        } else if (netz.getNetzStatus() == NetzStatus.GEBORGEN) {
            info.append("<b>Geborgen am:</b> ").append(geisternetzListe.getFormatiertesTatsaechlichesDatum(netz)).append("<br>");
            if (!geisternetzListe.istAnonymGeborgen(netz)) {
                Person berger = geisternetzListe.getErstenBerger(netz);
                if (berger != null) {
                    info.append("<b>Berger:</b> ").append(berger.getName()).append("<br>");
                }
            } else {
                info.append("<b>Berger:</b> Anonym<br>");
            }
        }
        
        // Bild hinzufügen, falls vorhanden
        if (netz.getBild() != null && !netz.getBild().isEmpty()) {
            info.append("<br><img src='").append(netz.getBild()).append("' style='max-width: 250px; max-height: 150px; border-radius: 5px;' alt='Geisternetz Bild'>");
        }
        
        info.append("</div>");
        return info.toString();
    }
    
    private String getStatusColor(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "red";
            case BERGUNGBEVORSTEHEND:
                return "yellow";
            case GEBORGEN:
                return "green";
            default:
                return "red";
        }
    }
    
    private String getStatusIcon(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "exclamation";
            case BERGUNGBEVORSTEHEND:
                return "clock";
            case GEBORGEN:
                return "check";
            default:
                return "exclamation";
        }
    }
    
    private String getStatusTextColor(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "#d32f2f";
            case BERGUNGBEVORSTEHEND:
                return "#f57c00";
            case GEBORGEN:
                return "#388e3c";
            default:
                return "#d32f2f";
        }
    }
    
    private String getStatusDisplayName(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "Gemeldet";
            case BERGUNGBEVORSTEHEND:
                return "Wird geborgen";
            case GEBORGEN:
                return "Geborgen";
            default:
                return status.toString();
        }
    }

    public MapModel getGeisternetzModel() {
        return geisternetzModel;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
        
        // Geisternetz-Nummer aus dem Marker-Titel extrahieren und Controller informieren
        if (selectedMarker != null && selectedMarker.getTitle() != null) {
            try {
                int geisternetzNummer = Integer.parseInt(selectedMarker.getTitle());
                bergungsController.setAktuellesGeisternetzByNummer(geisternetzNummer);
            } catch (NumberFormatException e) {
                System.err.println("Fehler beim Parsen der Geisternetz-Nummer: " + selectedMarker.getTitle());
            }
        }
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }
    
    // Hilfsmethode für die XHTML-Seite
    public Geisternetz getSelectedGeisternetz() {
        if (selectedMarker == null || selectedMarker.getTitle() == null) {
            return null;
        }
        
        try {
            int geisternetzNummer = Integer.parseInt(selectedMarker.getTitle());
            return geisternetzListe.getGeisternetze().stream()
                .filter(g -> g.getNr() == geisternetzNummer)
                .findFirst()
                .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}