package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Affected;

/**
 *
 * @author ibaca
 */
@Stateless
public class AffectedFacade extends AbstractFacade<Affected> implements AffectedFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public AffectedFacade() {
    super(Affected.class);
  }
}
