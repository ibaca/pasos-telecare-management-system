
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
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
        for (Alert alert : alerts.findAll()) {
            if (alert.getLatitude() != null) {
                LatLng latLng = new LatLng(alert.getLatitude(), alert.getLongitude());
                Marker marker = new Marker(latLng);
                marker.setTitle(alert.getCreated().toString() + ": " + alert.getCause());
                marker.setIcon("/tms-web/resources/img/skull.png");
                logger.info("Creado marker: " + marker);
                if (center == null) {
                    center = latLng;
                }
                markers.addOverlay(marker);
            }
        }
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
