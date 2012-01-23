package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Alert;

/**
 *
 * @author ibaca
 */
@Stateless
public class AlertFacade extends AbstractFacade<Alert> implements AlertFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AlertFacade() {
    super(Alert.class);
  }
}
