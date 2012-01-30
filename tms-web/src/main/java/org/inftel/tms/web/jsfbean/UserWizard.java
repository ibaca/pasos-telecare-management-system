/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jsfbean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.inftel.tms.domain.Affected;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.Person;
import org.inftel.tms.services.AffectedFacade;
import org.inftel.tms.services.DeviceFacade;
import org.inftel.tms.services.PeopleFacade;

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
    AffectedFacade affectedFacade = lookupAffectedFacadeRemote();
    @EJB
    DeviceFacade deviceFacade = lookupDeviceFacadeRemote();
    @EJB
    PeopleFacade peopleFacade = lookupPeopleFacadeRemote();
    
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
        List<Device> lista = null;
        lista.add(d);
        a.setDevices(lista);
        //Creamos los dos nuevos objetos
        peopleFacade.create(p);
        deviceFacade.create(d);
    }

    private PeopleFacade lookupPeopleFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (PeopleFacade) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/PeopleFacade!org.inftel.tms.services.PeopleFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private DeviceFacade lookupDeviceFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (DeviceFacade) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/DeviceFacade!org.inftel.tms.services.DeviceFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private AffectedFacade lookupAffectedFacadeRemote() {
        try {
            Context c = new InitialContext();
            return (AffectedFacade) c.lookup("java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/org.inftel.tms_tms-core_ejb_1.0-SNAPSHOT/AffectedFacade!org.inftel.tms.services.AffectedFacadeRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
