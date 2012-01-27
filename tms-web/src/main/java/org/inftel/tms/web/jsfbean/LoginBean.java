package org.inftel.tms.web.jsfbean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.inftel.tms.services.UserFacadeRemote;
import org.primefaces.context.RequestContext;

/**
 * 
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class LoginBean {
    @EJB
    private UserFacadeRemote userFacade;

    private String username;
    private String password;
    private boolean loggedIn = false;

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
        if (username != null && username.equals("admin") && password != null
                && password.equals("admin")) {
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Welcome", username);

        } else {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

    // public String goTo(){
    // if(loggedIn){
    // return "success-login";
    // }else{
    // return "bad-login";
    // }
    // }
}
