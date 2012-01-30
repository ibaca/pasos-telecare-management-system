package org.inftel.tms.services;

import java.util.List;

import javax.ejb.Local;
import org.inftel.tms.domain.Person;

/**
 *
 * @author ibaca
 */
@Local
public interface PeopleFacade {

  void create(Person people);

  void edit(Person people);

  void remove(Person people);

  Person find(Object id);

  List<Person> findAll();

  List<Person> findRange(int[] range);

  int count();
}
