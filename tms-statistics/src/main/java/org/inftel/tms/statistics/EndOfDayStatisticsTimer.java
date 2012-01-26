package org.inftel.tms.statistics;

import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.apache.commons.lang3.time.DateUtils;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AlertFacadeRemote;
import org.inftel.tms.utils.StatisticsDateUtil;

/**
 * Algunas estadisticas podrian generarse en el End Of Day, por ejemplo podrian
 * registarse diariamente el numero de alertas por tipo que se hay activas y que
 * hay inactivas.
 *
 * @author agumpg
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

        Date toDay = new Date();
        Date yesterday = StatisticsDateUtil.getYesterday();

        System.out.println("Timer event: " + toDay);

        //Actualización de diarios de Alertas
        processDiary(yesterday);

        //Si es el primer dia del mes lanzamos la actualización mensual de históricos de alertas
        Date firstDayToMonth = DateUtils.truncate(new Date(), Calendar.MONTH);
        if (DateUtils.isSameDay(firstDayToMonth, toDay)) {
            processMounthly(DateUtils.truncate(yesterday, Calendar.MONTH), yesterday);
        }

        //Si se ha producido un cambio de año actualizamos históricos anuales
        if (!StatisticsDateUtil.isEqualsYear(yesterday, toDay)) {
            processAnnual(toDay);
        }

    }

    /**
     * Cálculo del acumulado diario de los distintos tipos de alertas
     *
     * @param day fecha de cálculo
     */
    private void processDiary(Date day) {
        int countDaily;
        for (AlertType t : AlertType.values()) {
            countDaily = alertFacade.countByType(t, day, day);

            StatisticsData sd = new StatisticsData();

            sd.setName("Alert.type." + t.name().toLowerCase());
            sd.setDataPeriod(StatisticsData.statisticPeriod.DAYLY);
            sd.setLastDate(day);
            sd.setDataValue((long) countDaily);

            statisticsDataFacade.create(sd);
        }

    }

    /**
     * Procesamiento mensual de los distintos tipos de alertas
     *
     * @param toDate fecha límite inferior
     * @param fromDate fecha límite superior
     */
    private void processMounthly(Date toDate, Date fromDate) {
        int sum;
        for (AlertType t : AlertType.values()) {
            sum = statisticsDataFacade.sumStatictics("Alert.type." + t.name(),
                    StatisticsData.statisticPeriod.DAYLY,
                    toDate,
                    fromDate);

            StatisticsData sd = new StatisticsData();

            sd.setName("Alert.type." + t.name());
            sd.setDataPeriod(StatisticsData.statisticPeriod.MONTHLY);
            sd.setLastDate(new Date());
            sd.setDataValue((long) sum);

            statisticsDataFacade.create(sd);
        }

    }

    /**
     * Sumamos los acumulados mensuales (statisticPeriod.MONTHLY, desde
     * Febrero-Diciembre del año anterior mas el mes de Enero del año actual, ya
     * que Enero siempre contiene el sumatorio mensual del mes anterior, osea,
     * Diciembre.
     *
     * @param toDay fecha límite
     */
    private void processAnnual(Date toDay) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.MONTH, 1);
        Date oldDay = cal.getTime();


        int sumAnnual;
        for (AlertType t : AlertType.values()) {
            sumAnnual = statisticsDataFacade.sumStatictics("Alert.type." + t.name(),
                    StatisticsData.statisticPeriod.MONTHLY,
                    oldDay,
                    toDay);

            StatisticsData sd = new StatisticsData();

            sd.setName("Alert.type." + t.name());
            sd.setDataPeriod(StatisticsData.statisticPeriod.ANNUAL);
            sd.setLastDate(toDay);
            sd.setDataValue((long) sumAnnual);

            statisticsDataFacade.create(sd);
        }
    }
}
