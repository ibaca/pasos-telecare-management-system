/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.inftel.tms.domain.Alert;


/**
 *
 * @author inftel
 */
public class TableBean implements Serializable {
    private boolean show=true;
    private List<Alert> alerts;    
    private Alert selectedAlert;
    private String intervention;

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }
    
    
    public TableBean() {}

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    public void hide(){
        FacesMessage msg;
        if(show){
            msg=new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Alert", "Intervention opened");
            this.show=false;
        }else{
            //GUARDAR INTERVENCION
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Alert", "Intervention closed");
            this.show=true;
        }
        FacesContext.getCurrentInstance().addMessage(null, msg); 
    }
    
    public List<Alert> getAlerts() {
       // alerts=alertFacade.findActiveAlerts(); 
        alerts = new ArrayList<Alert>();
        Alert a = new Alert();
        a.setId(new Long(123123));
        a.setCause("asdfasdf");
        alerts.add(a);
        return alerts;
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
    
} 
