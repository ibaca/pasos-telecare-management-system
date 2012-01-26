/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.io.Serializable;  
import java.util.HashSet;  
import java.util.Set;  
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
  
import org.primefaces.context.RequestContext;  
  
public class ChatController implements Serializable {  
      
    private final static String CHANNEL = "chat";  
  
    private String message;  
      
    private String username;  
      
    private boolean loggedIn;  
      
    private Set<String> users = new HashSet<String>();  
  
    public String getMessage() {  
        return message;  
    }  
    public void setMessage(String message) {  
        this.message = message;  
    }  
      
    public String getUsername() {  
        return username;  
    }  
    public void setUsername(String username) {  
        this.username = username;  
    }  
      
    public boolean isLoggedIn() {  
        return loggedIn;  
    }  
    public void setLoggedIn(boolean loggedIn) {  
        this.loggedIn = loggedIn;  
    }  
  
    public void send() {  
        RequestContext.getCurrentInstance().push(CHANNEL, username + ": "+  message);  
          
        message = null;  
    }  
      
    public void login() {  
        RequestContext requestContext = RequestContext.getCurrentInstance();  
          
        if(users.contains(username)) {  
            loggedIn = false;  
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username taken", "Try with another username."));  
              
            requestContext.addPartialUpdateTarget("growl");  
        }  
        else {  
            users.add(username);  
            loggedIn = true;  
              
            requestContext.push(CHANNEL, username + " joined the channel.");  
        }  
    }  
      
    public void disconnect() {  
        RequestContext.getCurrentInstance().push(CHANNEL, username + " has left the channel.");  
        loggedIn = false;  
        username = null;  
    }  
}     
