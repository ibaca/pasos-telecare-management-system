package org.inftel.tms.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import org.inftel.tms.domain.Person;

/**
 *
 * @author ibaca
 */
@Local
public interface PeopleFacadeRemote {

  void create(Person people);

  void edit(Person people);

  void remove(Person people);

  Person find(Object id);

  List<Person> findAll();

  List<Person> findRange(int[] range);

  int count();
}
