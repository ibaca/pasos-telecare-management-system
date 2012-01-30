package org.inftel.tms.web.jsfbean;

/**
 * Bean usado para obtener estadisticos para ser mostrado como graficas.
 * @author Administrador
 */
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;
import static org.inftel.tms.statistics.StatisticDataPeriod.MONTHLY;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.inftel.tms.domain.AffectedType;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.services.AffectedFacade;
import org.inftel.tms.statistics.StatisticDataPeriod;
import org.inftel.tms.statistics.StatisticProcessor;
import org.primefaces.component.calendar.CalendarUtils;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean(name = "chartBean")
@RequestScoped
@SuppressWarnings("serial")
public class ChartBean implements Serializable {

    @EJB
    private AffectedFacade affectedFacade;
    @EJB
    private StatisticProcessor statistics;

    private PieChartModel affectedsModel;

    /** Alertas por mes, para cada tipo de alerta */
    private CartesianChartModel alertsModel;

    public ChartBean() {
    }

    public PieChartModel getAffectedsModel() {
        if (affectedsModel == null) {
            createCategoryModel();
        }
        return affectedsModel;
    }

    public CartesianChartModel getAlertsModel() {
        if (alertsModel == null) {
            createAlertsModel();
        }
        return alertsModel;
    }

    private void createCategoryModel() {
        affectedsModel = new PieChartModel(calculateAffectedPieData());
    }

    private void createAlertsModel() {
        alertsModel = new CartesianChartModel();
        for (AlertType alertType : AlertType.values()) {
            LineChartSeries series = new LineChartSeries(alertType.toString());
            String name = "alert.type." + alertType.name().toLowerCase();

            Map<Date, Long> samples; // todos los datos por fecha
            samples = statistics.findStatistics(name, DAYLY, new Date(0), new Date());
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));
            for (Date date : samples.keySet()) {
                series.set(df.format(date), samples.get(date));
            }

            alertsModel.addSeries(series);
        }
    }

    /**
     * Devuelve la lista de tipos de afectados junto el valor porcentual registrados en el sistema.
     */
    private Map<String, Number> calculateAffectedPieData() {
        Map<String, Number> result = new TreeMap<String, Number>();
        Long total = 0l;
        for (AffectedType type : AffectedType.values()) {
            long count = affectedFacade.countByType(type);
            total = total + count;
            result.put(type.toString(), count);
        }
        return result;
    }
}
