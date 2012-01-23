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
public class PeopleFacade extends AbstractFacade<Person> implements PeopleFacadeRemote {

  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public PeopleFacade() {
    super(Person.class);
  }
}
