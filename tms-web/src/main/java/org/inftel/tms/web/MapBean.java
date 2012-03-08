
package org.inftel.tms.web;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.inftel.tms.domain.Alert;
import org.inftel.tms.services.AlertFacade;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.*;

@ManagedBean
@ViewScoped
public class MapBean {

    private final static Logger logger = Logger.getLogger(MapBean.class.getName());

    @EJB
    private AlertFacade alerts;

    private MapModel markers = new DefaultMapModel();
    private LatLng center = null;

    @PostConstruct
    protected void initialize() {
        logger.info("Inicializando bean");
        Polyline tracking = new Polyline();
        for (Alert alert : alerts.findAll()) {
            if (alert.getLatitude() != null) {
                LatLng latLng = new LatLng(alert.getLatitude(), alert.getLongitude());
                Marker marker = new Marker(latLng);
                marker.setTitle(alert.getCreated().toString() + ": " + alert.getCause());
                String cause = (alert.getCause() != null) ? alert.getCause() : "sin causa";
                if (cause.startsWith("track")) {
                    marker.setIcon("/tms-web/resources/img/footprint.png");
                    tracking.getPaths().add(latLng);
                } else if (cause.startsWith("fence")) {
                    marker.setIcon("/tms-web/resources/img/stop.png");
                } else if (cause.startsWith("batt")) {
                    marker.setIcon("/tms-web/resources/img/poweroutage.png");
                } else if (cause.startsWith("click")) {
                    marker.setIcon("/tms-web/resources/img/skull.png");
                } else {
                    marker.setIcon("/tms-web/resources/img/flag-export.png");
                }
                
                logger.info("Creado marker: " + marker);
                if (center == null) {
                    center = latLng;
                }
                markers.addOverlay(marker);
            }
        }
        markers.addOverlay(tracking);
        if (center == null) {
            center = new LatLng(120.0, 34.0);
        }
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        Marker marker = (Marker) event.getOverlay();
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Selected",
                marker.getTitle()));
    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public MapModel getMarkers() {
        return markers;
    }

    public void setMarkers(MapModel markers) {
        this.markers = markers;
    }

}
