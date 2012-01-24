package org.inftel.tms.services;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.AlertType;
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

  @Override
  public int countByType(AlertType type, Date fromDate, Date toDate) {
    TypedQuery<Long> query = em.createNamedQuery(Alert.COUNT_BY_TYPE, Long.class);
    query.setParameter("type", type);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    return query.getSingleResult().intValue();
  }

  // Internal test usage
  AlertFacade(EntityManager em) {
    super(Alert.class);
    this.em = em;
  }
  
}
