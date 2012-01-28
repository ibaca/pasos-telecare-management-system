package org.inftel.tms.web.jsfbean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.Intervention;
import org.inftel.tms.domain.Person;
import org.inftel.tms.services.AlertFacadeRemote;
import org.inftel.tms.services.InterventionFacadeRemote;
import org.inftel.tms.services.UserFacadeRemote;
import org.inftel.tms.web.UserController;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * 
 * @author inftel
 */
@ManagedBean
@SessionScoped
public class DockBean implements Serializable {
    private boolean show = true;
    private Alert selectedAlert;
    private String intervention;
    private List<Alert> alerts;

    @EJB
    private AlertFacadeRemote alertFacade;
    @EJB
    private UserFacadeRemote userFacade;
    @EJB
    private InterventionFacadeRemote interFacade;

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }

    public DockBean() {
        System.out.println("nueva instancia table bean" + this);
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    public void hide() {
        FacesMessage msg;
        if (show) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Intervention opened", "Accesing...");
            this.show = false;
        } else {
            if (intervention != null || !intervention.isEmpty()) {
                Intervention newIntervention = new Intervention();
                newIntervention.setAlert(selectedAlert);
                newIntervention.setBy(userFacade.currentUser());
                newIntervention.setDescription(intervention);
                interFacade.create(newIntervention);

                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Intervention closed",
                        "Closing...");
                this.show = true;
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
                        "Can't close the intervention. Please fill the text area");
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Alert> getAlerts() {
        return alertFacade.findActiveAlerts();
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Alert getSelectedAlert() {
        return selectedAlert;
    }

    public void setSelectedAlert(Alert selectedAlert) {
        this.selectedAlert = selectedAlert;
    }

    public List<Person> getContacsOfSelectedAlert() {
        return this.selectedAlert.getAffected().getAffected().getContacts();
    }

    public MapModel getAdvancedModel() {
        MapModel advancedModel = new DefaultMapModel();
        
        // i am in...
        LatLng mycoord = new LatLng(selectedAlert.getAffected().getLatitude(),selectedAlert.getAffected().getLongitude());
        Marker myMark= new Marker(mycoord,selectedAlert.getAffected().getFirstName(),
                selectedAlert.getAffected().getSimpleName(),
                "http://maps.google.com/mapfiles/ms/micons/red-dot.png");
        advancedModel.addOverlay(myMark);
        
        // my people...
        for (Person p : getContacsOfSelectedAlert()) {
            LatLng coord = new LatLng(p.getLatitude(), p.getLongitude());
            Marker mark= new Marker(coord, p.getFirstName(), p.getSimpleName(),
                    "http://maps.google.com/mapfiles/ms/micons/blue-dot.png");
            advancedModel.addOverlay(mark);
        }

        return advancedModel;
    }
}
