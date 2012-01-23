package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Remote;
import org.inftel.tms.domain.Affected;

/**
 *
 * @author ibaca
 */
@Remote
public interface AffectedFacadeRemote {

  void create(Affected affected);

  void edit(Affected affected);

  void remove(Affected affected);

  Affected find(Object id);

  List<Affected> findAll();

  List<Affected> findRange(int[] range);

  int count();
}
