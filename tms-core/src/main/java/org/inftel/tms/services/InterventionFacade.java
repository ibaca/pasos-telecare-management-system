package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Intervention;

/**
 *
 * @author ibaca
 */
@Stateless
public class InterventionFacade extends AbstractFacade<Intervention> implements InterventionFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public InterventionFacade() {
    super(Intervention.class);
  }
}
