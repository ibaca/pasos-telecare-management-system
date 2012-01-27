package org.inftel.tms.statistics;

import java.util.Calendar;
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
  
  void updateStatistic(String statisticName, Calendar date, int value);
}
