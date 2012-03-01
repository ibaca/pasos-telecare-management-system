package org.inftel.tms.statistics;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

import org.inftel.tms.domain.AffectedType;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AffectedFacade;
import org.inftel.tms.services.AlertFacade;

@LocalBean
@Stateless
public class StatisticEndOfDayHandler {
	
	@EJB
    private AlertFacade alertFacade;
	
    @EJB
    private AffectedFacade affectedFacade;
   
    public void alertStatistics(@Observes StatisticEndOfDayEvent eodEvent) {
        Date from = eodEvent.getFromDate();
        Date to = eodEvent.getToDate();

        //eodEvent.updateData(statName, statSum, statCount);
        for (AlertType type : AlertType.values()) {
            Long statCount = alertFacade.countByType(type, from, to);
            String statName = "alert.type." + type.name().toLowerCase();
            eodEvent.updateData(statName, null, statCount);
        }

        // Generar estadisticas diarias de tipo de afectados registrados en el sistema
        for (AffectedType type : AffectedType.values()) {
            Long statCount = affectedFacade.countByType(type);
            String statName = "affected.type." + type.name().toLowerCase();
            eodEvent.updateData(statName, null, statCount);
        }  
    }

}