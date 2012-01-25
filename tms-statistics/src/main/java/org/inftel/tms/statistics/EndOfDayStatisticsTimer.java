package org.inftel.tms.statistics;

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
        System.out.println("Timer event: " + new Date());



        int valor = alertFacade.countByType(AlertType.USER, null, null); // alertas por tipo de usuario devuelve un entero

        StatisticsData sd = new StatisticsData();

        sd.setName("prueba");
        sd.setDataPeriod(StatisticsData.statisticPeriod.DAYLY);
        sd.setDataDate(null); //Fecha en milisegundos explicarlo. 
        sd.setDataValue(Long.MIN_VALUE);


        statisticsDataFacade.create(sd);


        


    }
}
