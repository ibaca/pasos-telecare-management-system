package org.inftel.tms.web.jsfbean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.inftel.tms.domain.User;
import org.inftel.tms.services.UserFacade;
import org.primefaces.context.RequestContext;

/**
 * 
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private UserFacade userFacade;

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
		List<User> lista = userFacade.findAll();
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg;
		for (User u : lista) {
			if (username != null && username.equals(u.getNickname()) && password != null
					&& password.equals(u.getPassword())) {
				loggedIn = true;
				msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Welcome", username);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				context.addCallbackParam("loggedIn", loggedIn);
				return;
			}
		}
		loggedIn = false;
		msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
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
