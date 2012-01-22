/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Remote;
import org.inftel.tms.domain.User;

/**
 *
 * @author ibaca
 */
@Remote
public interface UserFacadeRemote {

  void create(User user);

  void edit(User user);

  void remove(User user);

  User find(Object id);

  List<User> findAll();

  List<User> findRange(int[] range);

  int count();
  
}
