package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Person;

/**
 *
 * @author ibaca
 */
@Stateless
public class PeopleFacadeImpl extends AbstractFacade<Person> implements PeopleFacade {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public PeopleFacadeImpl() {
    super(Person.class);
  }
}
