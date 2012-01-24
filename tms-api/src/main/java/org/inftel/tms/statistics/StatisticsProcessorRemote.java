package org.inftel.tms.statistics;

import javax.ejb.Remote;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.Intervention;

/**
 *
 * @author ibaca
 */
@Remote
public interface StatisticsProcessorRemote {

  void processAlert(Alert name);

  void processIntervention(Intervention intervention);
  
}
