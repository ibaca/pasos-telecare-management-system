package org.inftel.tms.statistics;

import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AlertFacadeRemote;

/**
 * Algunas estadisticas podrian generarse en el End Of Day, por ejemplo podrian
 * registarse diariamente el numero de alertas por tipo que se hay activas y que
 * hay inactivas.
 */
@Stateless
@LocalBean
public class EndOfDayStatisticsTimer {

    @EJB
    private StatisticsDataFacade statisticsDataFacade;
    @EJB
    private AlertFacadeRemote alertFacade;

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0", dayOfWeek = "*")
    public void myTimer() {
        boolean changeMonth = false;
        int countDaily = 0;
        Date now = new Date();

        System.out.println("Timer event: " + now);

        //Actualización de diarios de Alertas
        for (AlertType t : AlertType.values()) {
            countDaily = alertFacade.countByType(t, now, now);

            StatisticsData sd = new StatisticsData();

            sd.setName("Alert");
            sd.setDataPeriod(StatisticsData.statisticPeriod.DAYLY);
            sd.setDataDate(now);
            sd.setDataValue((long) countDaily);

            statisticsDataFacade.create(sd);
        }




        if (changeMonth) {
        }



    }

    private Date getFirstDayToMonth() {

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMinimum(Calendar.DAY_OF_MONTH),
                cal.getMinimum(Calendar.HOUR_OF_DAY),
                cal.getMinimum(Calendar.MINUTE),
                cal.getMinimum(Calendar.SECOND));
        return cal.getTime();
    }

    private Date getLastDayToMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMaximum(Calendar.DAY_OF_MONTH),
                cal.getMaximum(Calendar.HOUR_OF_DAY),
                cal.getMaximum(Calendar.MINUTE),
                cal.getMaximum(Calendar.SECOND));
        return cal.getTime();
    }

    private Date getFirstDayToAnnual() {

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMinimum(Calendar.DAY_OF_MONTH),
                cal.getMinimum(Calendar.HOUR_OF_DAY),
                cal.getMinimum(Calendar.MINUTE),
                cal.getMinimum(Calendar.SECOND));
        return cal.getTime();
    }
}
