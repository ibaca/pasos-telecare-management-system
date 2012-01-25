/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jspbean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.services.UserFacadeRemote;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrador
 */
public class LoginBean {
    @EJB
    private UserFacadeRemote userFacade;
  
    private String username; 
    private String password;
    private boolean loggedIn=false;
      
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {
        this.password = password;  
    }  
  
    public void login() { 
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg;
        if(username != null  && username.equals("admin") && password != null  && password.equals("admin")) {  
            loggedIn = true;  
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
            
        } else {  
            loggedIn = false;  
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
        } 
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        context.addCallbackParam("loggedIn", loggedIn);  
    }
    
//    public String goTo(){
//        if(loggedIn){
//            return "success-login";
//        }else{
//            return "bad-login";
//        }
//    }
}  
