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
public class AlertRawFacadeImpl extends AbstractFacade<AlertRaw> implements AlertRawFacade {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AlertRawFacadeImpl() {
    super(AlertRaw.class);
  }
}
