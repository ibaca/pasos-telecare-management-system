package org.inftel.tms.statistics;

import static org.inftel.tms.statistics.StatisticDataPeriod.ANNUAL;
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;
import static org.inftel.tms.statistics.StatisticDataPeriod.MONTHLY;

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

            create("alert.type." + t.name().toLowerCase(), DAYLY, day, (long) countDaily );
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
                    StatisticDataPeriod.DAYLY,
                    toDate,
                    fromDate);

            create("alert.type." + t.name(), MONTHLY,  new Date(), (long) sum);
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
                    StatisticDataPeriod.MONTHLY,
                    oldDay,
                    toDay);

            create("alert.type." + t.name(), ANNUAL, toDay, (long) sumAnnual);
        }
    }

    private void create(String name, StatisticDataPeriod period, Date toDay, long value) {
        StatisticsData sd = new StatisticsData();

        sd.setName(name);
        sd.setDataPeriod(period);
        sd.setLastDate(toDay);
        sd.setDataValue(value);

        statisticsDataFacade.create(sd);
    }
}
