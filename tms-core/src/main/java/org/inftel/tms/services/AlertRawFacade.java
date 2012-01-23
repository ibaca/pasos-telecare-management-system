package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.AlertRaw;

/**
 *
 * @author ibaca
 */
@Stateless
public class AlertRawFacade extends AbstractFacade<AlertRaw> implements AlertRawFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AlertRawFacade() {
    super(AlertRaw.class);
  }
}
