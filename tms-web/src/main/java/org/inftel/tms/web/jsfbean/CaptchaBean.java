/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
import javax.faces.event.ActionEvent;  
  

/**
 *
 * @author Cristian
 */
@ManagedBean
@RequestScoped
public class CaptchaBean {

    /**
     * Creates a new instance of CaptchaBean
     */
    public CaptchaBean() {
    }
    public void submit(ActionEvent event) {  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    } 
}
