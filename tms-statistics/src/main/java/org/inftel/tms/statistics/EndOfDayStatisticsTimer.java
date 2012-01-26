package org.inftel.tms.statistics;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
//import org.apache.commons.lang3.time.DateUtils;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AlertFacadeRemote;
import org.inftel.tms.utils.StatisticsDateUtil;

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
        int countDaily;

        Date yesterday = StatisticsDateUtil.getYesterday();
        //DateUtils.truncate(yesterday, Calendar.MONTH);
        System.out.println("Timer event: " + new Date());

        //Actualización de diarios de Alertas
        for (AlertType t : AlertType.values()) {
            countDaily = alertFacade.countByType(t, yesterday, yesterday);

            StatisticsData sd = new StatisticsData();

            sd.setName("Alert.type." + t.name().toLowerCase());
            sd.setDataPeriod(StatisticsData.statisticPeriod.DAYLY);
            sd.setLastDate(yesterday);
            sd.setDataValue((long) countDaily);

            statisticsDataFacade.create(sd);
        }




//        if (changeMonth) {
//
//            //pensar: si el mes actual no tiene anotación mounthly calcular mes anterior, sino el cálculo ya se hizo para ese mes.
//            Date firstDayMonth = new Date(); //calcular el primer día del mes
//            Date lastDayMonth = new Date(); // calcular el ultimo día del mes
//            int sum;
//
//            //Actualización de estadísticas mensuales para cada tipo de alarma.    
//            for (AlertType t : AlertType.values()) {
//
//                sum = statisticsDataFacade.sumStatictics("Alert.type."+t.name(), StatisticsData.statisticPeriod.DAYLY, firstDayMonth, lastDayMonth);
//
//                StatisticsData sd = new StatisticsData();
//
//                sd.setName("Alert.type.user");
//                sd.setDataPeriod(StatisticsData.statisticPeriod.MONTHLY);
//                sd.setLastDate(lastDayMonth);
//                sd.setDataValue((long) sum);
//
//                statisticsDataFacade.create(sd);
//
//            }
//
//    }
}
}
