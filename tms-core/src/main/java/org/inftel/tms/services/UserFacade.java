/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.User;

/**
 *
 * @author ibaca
 */
@Stateless
public class UserFacade extends AbstractFacade<User> implements UserFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  protected EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UserFacade() {
    super(User.class);
  }
  // FIXME los siguientes metodos se han agregado solo para mostrar un ejemplo
  // de simulacion de servicios en tests
  @EJB
  private DeviceFacadeRemote deviceFacade;

  protected DeviceFacadeRemote getDeviceFacade() {
    return deviceFacade;
  }

  @Override
  public List<Device> getDevices() {
    // Solo para demostrar el test!
    return getDeviceFacade().findAll();
  }
  
  
}
