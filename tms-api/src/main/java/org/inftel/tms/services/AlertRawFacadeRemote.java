package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Remote;
import org.inftel.tms.domain.AlertRaw;

/**
 *
 * @author ibaca
 */
@Remote
public interface AlertRawFacadeRemote {

  void create(AlertRaw alertsRaw);

  void edit(AlertRaw alertsRaw);

  void remove(AlertRaw alertsRaw);

  AlertRaw find(Object id);

  List<AlertRaw> findAll();

  List<AlertRaw> findRange(int[] range);

  int count();
}
