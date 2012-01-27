/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.tms.domain.Affected;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.Person;
import org.inftel.tms.domain.User;
import org.inftel.tms.services.AffectedFacadeRemote;
import org.inftel.tms.services.DeviceFacadeRemote;
import org.inftel.tms.services.PeopleFacadeRemote;

/**
 *
 * @author inftel
 */
/**
 *
 * @author inftel
 */
@ManagedBean
public class UserWizard {
    @EJB
    AffectedFacadeRemote affectedFacade = lookupAffectedFacadeRemote();
    @EJB
    DeviceFacadeRemote deviceFacade = lookupDeviceFacadeRemote();
    @EJB
    PeopleFacadeRemote peopleFacade = lookupPeopleFacadeRemote();
    
    Person p;
    Device d;
    Affected a;
    
    private String Firstname;
    private String Lastname;
    private String email;
    private String Mobilenumber;

    public void save (){
        //Inicialiamos la persona
        a = new Affected();
        p= new Person();
        p.setFirstName(Firstname);
        p.setLastName(Lastname);
        p.setEmail(email);
        p.setMobileNumber(Mobilenumber);
        //Inicializamos el dispositivo
        d=new Device();
        d.setMobileNumber(Mobilenumber);
        a.setData(p);
        Set<Device> lista = null;
        lista.add(d);
        a.setDevices(lista);
        //Creamos los dos nuevos objetos
        peopleFacade.create(p);
        deviceFacade.create(d);
    }

    private PeopleFacadeRemote lookupPeopleFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (PeopleFacadeRemote) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/PeopleFacade!org.inftel.tms.services.PeopleFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private DeviceFacadeRemote lookupDeviceFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (DeviceFacadeRemote) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/DeviceFacade!org.inftel.tms.services.DeviceFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private AffectedFacadeRemote lookupAffectedFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (AffectedFacadeRemote) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/AffectedFacade!org.inftel.tms.services.AffectedFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
