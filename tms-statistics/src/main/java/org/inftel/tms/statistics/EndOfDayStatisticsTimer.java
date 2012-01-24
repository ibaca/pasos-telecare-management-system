package org.inftel.tms.statistics;

import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 * Algunas estadisticas podrian generarse en el End Of Day, por ejemplo podrian registarse
 * diariamente el numero de alertas por tipo que se hay activas y que hay inactivas.
 */
@Stateless
@LocalBean
public class EndOfDayStatisticsTimer {

  @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0", dayOfWeek = "*")
  public void myTimer() {
    System.out.println("Timer event: " + new Date());
  }
  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
}
