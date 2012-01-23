package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Remote;
import org.inftel.tms.domain.Alert;

/**
 *
 * @author ibaca
 */
@Remote
public interface AlertFacadeRemote {

  void create(Alert alerts);

  void edit(Alert alerts);

  void remove(Alert alerts);

  Alert find(Object id);

  List<Alert> findAll();

  List<Alert> findRange(int[] range);

  int count();
}
