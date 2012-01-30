package org.inftel.tms.statistics;

import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inftel.tms.domain.AffectedType;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AffectedFacade;
import org.inftel.tms.services.AlertFacade;
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;

/**
 * Algunas estadisticas podrian generarse en el End Of Day, por ejemplo podrian registarse
 * diariamente el numero de alertas por tipo que se hay activas y que hay inactivas.
 * 
 * @author agumpg
 */
@Stateless
@LocalBean
public class StatisticEndOfDay {

    @EJB
    private StatisticProcessorImpl statisticProcessor;
    @EJB
    private AlertFacade alertFacade;
    @EJB
    private AffectedFacade affectedFacade;

    /**
     * Calcula las estadisticas diarias para algunos valores. Y ademas, genera los historicos de
     * periodos superiores al diario. Por tanto, es importante que la cola de mensajes este vacia
     * para que todos esten procesados previamente.
     * TODO comprobar que la cola esta vacia
     */
    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "2", dayOfWeek = "*")
    public void processDialyStatistics() {

        Calendar yesterday = Calendar.getInstance();
        yesterday.setTime(StatisticDateUtils.getYesterday());
        Date from = DAYLY.beginsAt(yesterday).getTime();
        Date to = DAYLY.endsAt(yesterday).getTime();

        generateEndOfDayStatistics(yesterday, from, to);
        statisticProcessor.updatePeriodsForAllStatistics();

    }

    private void generateEndOfDayStatistics(Calendar yesterday, Date from, Date to) {
        // Generar estadisticas diarias de tipo de alertas recibidas ayer
        for (AlertType type : AlertType.values()) {
            Long statCount = alertFacade.countByType(type, from, to);
            String statName = "alert.type." + type.name().toLowerCase();

            StatisticData data = new StatisticData(statName, yesterday.getTime(), null, statCount);
            statisticProcessor.updateDaylyStatistic(data);
        }

        // Generar estadisticas diarias de tipo de afectados registrados en el sistema
        for (AffectedType type : AffectedType.values()) {
            Long statCount = affectedFacade.countByType(type);
            String statName = "affected.type." + type.name().toLowerCase();

            StatisticData data = new StatisticData(statName, yesterday.getTime(), null, statCount);
            statisticProcessor.updateDaylyStatistic(data);
        }
    }
}
