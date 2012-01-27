package org.inftel.tms.web.jsfbean;

/**
 *
 * @author Administrador
 */
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.inftel.tms.domain.AffectedType;
import org.inftel.tms.services.AffectedFacadeRemote;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean(name = "chartBean")
@RequestScoped
@SuppressWarnings("serial")
public class ChartBean implements Serializable {

    @EJB
    private AffectedFacadeRemote affectedFacade;

    private PieChartModel affectedsModel;

    private CartesianChartModel linearModel;

    public ChartBean() {
    }

    public PieChartModel getAffectedsModel() {
        if (affectedsModel == null) {
            createCategoryModel();
        }
        return affectedsModel;
    }

    public CartesianChartModel getLinearModel() {
        if (linearModel == null) {
            createLinearModel();
        }
        return linearModel;
    }

    private void createCategoryModel() {
        affectedsModel = new PieChartModel(calculateAffectedPieData());
    }

    private void createLinearModel() {
        linearModel = new CartesianChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");
        series2.setMarkerStyle("diamond");

        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);

        linearModel.addSeries(series1);
        linearModel.addSeries(series2);
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
