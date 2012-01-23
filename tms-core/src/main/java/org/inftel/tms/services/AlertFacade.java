package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.Person;

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

  @Override
  public List<Alert> findActiveAlerts() {
    return em.createNamedQuery(Alert.FIND_ACTIVED, Alert.class).getResultList();
  }

  @Override
  public List<Alert> findAlertsByAffected(Person afected) {
    Query query = em.createNamedQuery(Alert.FIND_BY_AFFECTED, Alert.class);
    query.setParameter("affected", afected);
    return query.getResultList();
  }
  
}
