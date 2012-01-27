package org.inftel.tms.statistics;

import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inftel.tms.domain.AffectedType;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AffectedFacadeRemote;
import org.inftel.tms.services.AlertFacadeRemote;
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;
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
    private StatisticsProcessorLocal statisticsProcessor;
    @EJB
    private AlertFacadeRemote alertFacade;
    @EJB
    private AffectedFacadeRemote affectedFacade;

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0", dayOfWeek = "*")
    public void processDialyStatistics() {

        Calendar yesterday = Calendar.getInstance();
        yesterday.setTime(StatisticsDateUtil.getYesterday());

        System.out.println("Timer event: " + new Date());

        // Generar estadisticas diarias de tipo de alertas recibidas ayer
        for (AlertType type : AlertType.values()) {
            Date from = DAYLY.beginsAt(yesterday).getTime();
            Date to = DAYLY.endsAt(yesterday).getTime();
            int statCount = alertFacade.countByType(type, from, to);
            String statName = "alert.type." + type.name().toLowerCase();

            statisticsProcessor.updateStatistic(statName, yesterday, statCount);
        }

        // Generar estadisticas diarias de tipo de afectados registrados en el sistema
        for (AffectedType type : AffectedType.values()) {
            int statCount = affectedFacade.countByType(type);
            String statName = "affected.type." + type.name().toLowerCase();

            statisticsProcessor.updateStatistic(statName, yesterday, statCount);
        }

    }
}
