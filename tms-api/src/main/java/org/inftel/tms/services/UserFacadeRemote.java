package org.inftel.tms.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.User;

/**
 *
 * @author ibaca
 */
@Local
public interface UserFacadeRemote {

  void create(User user);

  void edit(User user);

  void remove(User user);

  User find(Object id);

  List<User> findAll();

  List<User> findRange(int[] range);

  int count();

  List<Device> getDevices();

  User currentUser();
}
