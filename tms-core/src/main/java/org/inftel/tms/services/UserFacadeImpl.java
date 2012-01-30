package org.inftel.tms.services;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.User;

/**
 *
 * @author ibaca
 */
@Stateless
public class UserFacadeImpl extends AbstractFacade<User> implements UserFacade {

  @PersistenceContext(unitName = "tms-persistence")
  protected EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UserFacadeImpl() {
    super(User.class);
  }
  // FIXME los siguientes metodos se han agregado solo para mostrar un ejemplo
  // de simulacion de servicios en tests
  @EJB
  private DeviceFacade deviceFacade;

  protected DeviceFacade getDeviceFacade() {
    return deviceFacade;
  }

  @Override
  public List<Device> getDevices() {
    // Solo para demostrar el test!
    return getDeviceFacade().findAll();
  }

  /**
   * Uso interno para configurar test
   */
  UserFacadeImpl(EntityManager em, DeviceFacade deviceFacade) {
    super(User.class);
    this.em = em;
    this.deviceFacade = deviceFacade;
  }

  /**
   * Devuelve el usuario dado de alta en el sistema.
   *
   * FIXME por ahora siempre devuelve el mismo usuario para simplificar (o anular) la gestion de
   * usuarios. Deberia delegar la responsabilidad a cada implementacion concreta, por ejemplo
   * glassfish.
   *
   * @return usuario dado de alta en el sistema
   */
  @Override
  public User currentUser() {
    User current;
    List<User> users = findAll();
    if (users.size() == 0) {
      current = new User();
      current.setNickname("admin");
      current.setEmail("admin@mail.com");
      current.setFullName("Administrator Default User");
      current.setPassword("admin");
      current.setUserRole(User.Role.ADMIN);
      em.getTransaction().begin();
      create(current);
      em.getTransaction().commit();
    } else {
      current = users.get(0);
    }
    return current;
  }
}
