import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@Named
@ViewScoped
public class GeisternetzMapView implements Serializable {
    private static final long serialVersionUID = 1L;

    private MapModel geisternetzModel;
    private Marker selectedMarker;
    private Geisternetz selectedGeisternetz;
    private Set<NetzStatus> selectedStatuses;

    private boolean showGemeldet = true;
    private boolean showBergungbevorstehend = true;
    private boolean showGeborgen = true;
    private boolean showVerschollen = true;

    @Inject private GeisternetzDAO geisternetzDAO;
    @Inject private GeisternetzListe geisternetzListe;
    @Inject private MeldungsController meldungsController;
    @Inject private BergungsController bergungsController;
    @Inject private VerschollenDAO verschollenDAO;

    @PostConstruct
    public void init() {
        selectedStatuses = new HashSet<>(Arrays.asList(NetzStatus.values()));
        refreshMapModel();
    }

    public void refreshMapModel() {
        geisternetzModel = new DefaultMapModel();

        for (Geisternetz netz : geisternetzListe.getGeisternetze()) {
            if (!selectedStatuses.contains(netz.getNetzStatus())) continue;

            meldungsController.ladeMeldungenZuGeisternetz(netz);

            LatLng koordinaten = new LatLng(netz.getLat(), netz.getLng());
            Marker marker = new Marker(koordinaten, String.valueOf(netz.getNr()), createInfoString(netz));
            marker.setIcon("http://maps.google.com/mapfiles/ms/icons/" + getStatusColor(netz.getNetzStatus()) + "-dot.png");
            geisternetzModel.addOverlay(marker);
        }
    }

    public void updateFilter() {
        selectedStatuses.clear();
        if (showGemeldet) selectedStatuses.add(NetzStatus.GEMELDET);
        if (showBergungbevorstehend) selectedStatuses.add(NetzStatus.BERGUNGBEVORSTEHEND);
        if (showGeborgen) selectedStatuses.add(NetzStatus.GEBORGEN);
        if (showVerschollen) selectedStatuses.add(NetzStatus.VERSCHOLLEN);

        refreshMapModel();
        refreshUIComponent("geisternetzMap");
    }

    public void showAllStatuses() {
        setAllStatuses(true);
        updateFilter();
    }

    public void hideAllStatuses() {
        setAllStatuses(false);
        updateFilter();
    }

    public void showOnlyStatus(NetzStatus status) {
        hideAllStatuses();
        switch (status) {
            case GEMELDET:                
                showGemeldet = true;
                break;
            case BERGUNGBEVORSTEHEND:      
                showBergungbevorstehend = true;
                break;
            case GEBORGEN:                
                showGeborgen = true;
                break;
            case VERSCHOLLEN:             
                showVerschollen = true;
                break;
        }
        updateFilter();
    }



    public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
        selectedGeisternetz = findGeisternetzByMarker(selectedMarker);
        if (selectedGeisternetz != null) {
            bergungsController.setAktuellesGeisternetzByNummer(selectedGeisternetz.getNr());
        }
    }

    public void forceMapRefresh() {
        init();
        refreshUIComponent("@form");
    }


    private void setAllStatuses(boolean value) {
        showGemeldet = value;
        showBergungbevorstehend = value;
        showGeborgen = value;
        showVerschollen = value;
    }

    private void refreshUIComponent(String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getPartialViewContext().isAjaxRequest()) {
            context.getPartialViewContext().getRenderIds().add(clientId);
        }
    }

    private Geisternetz findGeisternetzByMarker(Marker marker) {
        if (marker != null && marker.getTitle() != null) {
            try {
                int nr = Integer.parseInt(marker.getTitle());
                return geisternetzListe.getGeisternetze().stream()
                        .filter(g -> g.getNr() == nr)
                        .findFirst()
                        .orElse(null);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }


    private String createInfoString(Geisternetz netz) {
        StringBuilder info = new StringBuilder("<div style='max-width: 300px;'>");
        info.append("<b>Nr:</b> ").append(netz.getNr()).append("<br>")
            .append("<b>Größe:</b> ").append(netz.getGroeße()).append("<br>")
            .append("<b>Status:</b> <span style='color: ").append(getStatusTextColor(netz.getNetzStatus())).append(";'>")
            .append(getStatusDisplayName(netz.getNetzStatus())).append("</span><br>")
            .append("<b>Standort:</b> ").append(netz.getStandort()).append("<br>")
            .append("<b>Koordinaten:</b> ").append(netz.getLat()).append(", ").append(netz.getLng()).append("<br>")
            .append("<b>Gemeldet am:</b> ").append(meldungsController.getFormatiertesMeldungsDatum(netz)).append("<br>");

        appendStatusSpecificInfo(info, netz);
        if (netz.getBild() != null && !netz.getBild().isEmpty()) {
            info.append("<br><img src='").append(netz.getBild())
                .append("' style='max-width: 250px; max-height: 150px; border-radius: 5px;' alt='Geisternetz Bild'>");
        }
        info.append("</div>");
        return info.toString();
    }

    private void appendStatusSpecificInfo(StringBuilder info, Geisternetz netz) {
        switch (netz.getNetzStatus()) {
            case GEMELDET:
                appendMelderInfo(info, netz);
                break;
            case VERSCHOLLEN:
                appendVerschollenInfo(info, netz);
                break;
            case BERGUNGBEVORSTEHEND:
                appendBergungInfo(info, netz, true);
                break;
            case GEBORGEN:
                appendBergungInfo(info, netz, false);
                break;
        }
    }

    private void appendMelderInfo(StringBuilder info, Geisternetz netz) {
        if (!meldungsController.istAnonymGemeldet(netz)) {
            Person melder = meldungsController.getErstenMelder(netz);
            if (melder != null) info.append("<b>Melder:</b> ").append(melder.getName()).append("<br>");
        } else {
            info.append("<b>Melder:</b> Anonym<br>");
        }
    }

    private void appendVerschollenInfo(StringBuilder info, Geisternetz netz) {
        List<Verschollen> verschollenMeldungen = verschollenDAO.getVerschollenMeldungenByGeisternetz(netz);
        if (!verschollenMeldungen.isEmpty()) {
            Verschollen ersteMeldung = verschollenMeldungen.get(0);
            info.append("<b>Als verschollen gemeldet am:</b> ")
                .append(ersteMeldung.getMeldungsDatum().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("<br>")
                .append("<b>Verschollen gemeldet von:</b> ")
                .append(ersteMeldung.getMelder() != null ? ersteMeldung.getMelder().getName() : "Anonym").append("<br>");
        }
    }

    private void appendBergungInfo(StringBuilder info, Geisternetz netz, boolean geplant) {
        if (geplant) {
            info.append("<b>Geplante Bergung:</b> ").append(geisternetzListe.getFormatiertesGeplanteDatum(netz)).append("<br>");
        } else {
            info.append("<b>Geborgen am:</b> ").append(geisternetzListe.getFormatiertesTatsaechlichesDatum(netz)).append("<br>");
        }
        Person berger = geisternetzListe.getErstenBerger(netz);
        if (berger != null) info.append("<b>Berger:</b> ").append(berger.getName()).append("<br>");
    }


    private String getStatusColor(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "red";
            case BERGUNGBEVORSTEHEND:
                return "purple";
            case GEBORGEN:
                return "blue";
            case VERSCHOLLEN:
                return "orange";
            default:
                return "red"; 
        }
    }

    private String getStatusTextColor(NetzStatus status) {
        switch (status) {
            case GEMELDET:
                return "#d32f2f";
            case BERGUNGBEVORSTEHEND:
                return "#8A2BE2";
            case GEBORGEN:
                return "#4169E1";
            case VERSCHOLLEN:
                return "#FFA500";
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
            case VERSCHOLLEN:
                return "Verschollen";
            default:
                return status.toString();
        }
    }



    public MapModel getGeisternetzModel() { return geisternetzModel; }
    public Marker getSelectedMarker() { return selectedMarker; }
    public Geisternetz getSelectedGeisternetz() { return selectedGeisternetz; }
    public boolean isShowGemeldet() { return showGemeldet; }
    public void setShowGemeldet(boolean showGemeldet) { this.showGemeldet = showGemeldet; }
    public boolean isShowBergungbevorstehend() { return showBergungbevorstehend; }
    public void setShowBergungbevorstehend(boolean showBergungbevorstehend) { this.showBergungbevorstehend = showBergungbevorstehend; }
    public boolean isShowGeborgen() { return showGeborgen; }
    public void setShowGeborgen(boolean showGeborgen) { this.showGeborgen = showGeborgen; }
    public boolean isShowVerschollen() { return showVerschollen; }
    public void setShowVerschollen(boolean showVerschollen) { this.showVerschollen = showVerschollen; }
    public long getAnzahlGemeldet() {
        return geisternetzListe.getGeisternetze().stream()
                .filter(g -> g.getNetzStatus() == NetzStatus.GEMELDET)
                .count();
    }

    public long getAnzahlBergungbevorstehend() {
        return geisternetzListe.getGeisternetze().stream()
                .filter(g -> g.getNetzStatus() == NetzStatus.BERGUNGBEVORSTEHEND)
                .count();
    }

    public long getAnzahlGeborgen() {
        return geisternetzListe.getGeisternetze().stream()
                .filter(g -> g.getNetzStatus() == NetzStatus.GEBORGEN)
                .count();
    }

    public long getAnzahlVerschollen() {
        return geisternetzListe.getGeisternetze().stream()
                .filter(g -> g.getNetzStatus() == NetzStatus.VERSCHOLLEN)
                .count();
    }

}
