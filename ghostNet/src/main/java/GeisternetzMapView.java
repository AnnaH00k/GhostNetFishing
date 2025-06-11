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

            String info = "<b>Ort:</b> " + netz.getStandort() +
                          "<br/><b>Größe:</b> " + netz.getGroeße() +
                          "<br/><b>Status:</b> " + netz.getNetzStatus();

            Marker marker = new Marker(koordinaten, "Geisternetz #" + netz.getNr(), info);
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
