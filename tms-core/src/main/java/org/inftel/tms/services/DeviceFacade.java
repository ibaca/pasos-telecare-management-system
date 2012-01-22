/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Device;

/**
 *
 * @author ibaca
 */
@Stateless
public class DeviceFacade extends AbstractFacade<Device> implements DeviceFacadeRemote {
  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public DeviceFacade() {
    super(Device.class);
  }
  
}
