import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
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

    @PostConstruct
    public void init() {
        geisternetzModel = new DefaultMapModel();

        for (Geisternetz netz : geisternetzListe.getGeisternetze()) {
            LatLng koordinaten = new LatLng(netz.getLat(), netz.getLng());

            String info =
                "<b>Nr:</b> " + netz.getNr() + "<br>" +
                "<b>Größe:</b> " + netz.getGroeße() + "<br>" +
                "<b>Status:</b> " + netz.getNetzStatus() + "<br>" +
                "<b>Standort:</b> " + netz.getStandort() + "<br>" +
                "<b>Breitengrad:</b> " + netz.getLat() + "<br>" +
                "<b>Längengrad:</b> " + netz.getLng() + "<br>"+ "<br>";
            
            // Bild hinzufügen, falls vorhanden
            if (netz.getBild() != null && !netz.getBild().isEmpty()) {
                info += "<img src='" + netz.getBild() + "' style='max-width: 200px; max-height: 150px;' alt='Geisternetz Bild'>";
            }

            // Titel sollte die Nummer sein, nicht das Bild
            Marker marker = new Marker(koordinaten, "" , info);
            geisternetzModel.addOverlay(marker);
        }
    }

    public MapModel getGeisternetzModel() {
        return geisternetzModel;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }
}
