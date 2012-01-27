/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.services.AlertFacadeRemote;


/**
 *
 * @author inftel
 */
public class TableBean implements Serializable {
    private boolean show=true;   
    private Alert selectedAlert;
    private String intervention;
    private List<Alert> alerts;
    
    @EJB
    private AlertFacadeRemote alertFacade;

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
            msg=new FacesMessage(FacesMessage.SEVERITY_WARN, "Intervention opened", "Accesing..");
            this.show=false;
        }else{
            System.out.println("INTERVENTIONNNNN: "+intervention);
            if(intervention!=null || !intervention.isEmpty()){
                //GUARDAR INTERVENCION
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Intervention closed", "Closing..");
                this.show=true;
            }else{
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Can't close the intervention. Please fill the text area");
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg); 
    }
    
    public List<Alert> getAlerts() {
        return alertFacade.findActiveAlerts(); 
    }
    
    public void setAlerts(List<Alert> alerts){
        this.alerts=alerts;
    }

    public Alert getSelectedAlert() {
        return selectedAlert;
    }

    public void setSelectedAlert(Alert selectedAlert) {
        this.selectedAlert = selectedAlert;
    }
    
} 
