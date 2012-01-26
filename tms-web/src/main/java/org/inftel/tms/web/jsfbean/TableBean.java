/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.services.AlertFacadeRemote;

/**
 *
 * @author inftel
 */
public class TableBean implements Serializable {
    @EJB
    private AlertFacadeRemote alertFacade;
    private boolean show=true;
    private List<Alert> alerts;    
    private Alert selectedAlert;
    
    public TableBean() {}

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    public void hide(){
        this.show=false;
    }
    
    public void unHide(){
        this.show=true;
    }
    
    public List<Alert> getAlerts() {
        alerts=alertFacade.findActiveAlerts(); 
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
