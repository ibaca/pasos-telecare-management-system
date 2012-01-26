package org.inftel.tms.statistics;

import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author agumpg
 */
@Stateless
public class StatisticsDataFacade extends AbstractFacade<StatisticsData> {

    @PersistenceContext(unitName = "tms-statistic-unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatisticsDataFacade() {
        super(StatisticsData.class);
    }

    /**
     * Suma las estadísticas diarias, mensuales o anuales de un determinado
     * valor entre un rango de fechas
     *
     * @param startWith nombre del atributo estadístico
     * @param period diario, mensual o anual
     * @param fromDate fecha de inicio del sumatorio
     * @param toDate fecha fin del sumatorio
     * @return sumatorio de los estadísticos
     */
    public int sumStatictics(String startWith, StatisticsData.statisticPeriod period, Date fromDate, Date toDate) {
        TypedQuery<StatisticsData> query = em.createQuery("SELECT o FROM StatisticsData o WHERE o.name LIKE :name AND o.dataPeriod = :period AND o.lastDate BETWEEN :fromDate AND :toDate", StatisticsData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<StatisticsData> result = query.getResultList();
        int sum = 0;
        for (StatisticsData s : result) {
            sum += s.getDataValue();
        }

        return sum;
    }

    /**
     * Busca todos los valores estadisticos que comiencen con el nombre pasado
     * como parametro.
     *
     * @param startWith nombre estadistico que filtra los tipos que se quieren
     * obtener
     * @param period periodo estadistico que se quiere obtener
     * @param date fecha del periodo que se quiere obtener
     * @return parejas de nombre de estadistica y valor para el periodo y facha
     * pasados
     */
    public Map<String, Long> findStatistics(String startWith, StatisticsData.statisticPeriod period, Date date) {
        TypedQuery<StatisticsData> query = em.createQuery("SELECT o FROM StatisticsData o WHERE o.name LIKE :name AND o.dataPeriod = :period AND o.lastDate = :date", StatisticsData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("date", date);
        Map<String, Long> result = new TreeMap<String, Long>();
        for (StatisticsData data : query.getResultList()) {
            result.put(data.getName(), data.getDataValue());
        }
        return result;
    }

    /**
     * Busca el intervalo de datos estadisticos para los nombres de estadisticas
     * que comiencen con el nombre pasado como parametro en un rango de fechas
     *
     * @param startWith nombre del estadistico que filtra los tipos que se
     * quieren obtener (i.e. alert.type.user), patrón nombre%
     * @param period periodo estadistico que se quiere obtener
     * [DAILY|MOUNTHLY|ANNUAL]
     * @param fromDate inicio de intervalo
     * @param toDate fin de intervalo
     * @return
     */
    public List<StatisticsData> findStatistics(String startWith, StatisticsData.statisticPeriod period, Date fromDate, Date toDate) {
        TypedQuery<StatisticsData> query = em.createQuery("SELECT o FROM StatisticsData o WHERE o.name LIKE :name AND o.dataPeriod = :period AND o.lastDate BETWEEN :fromDate AND :toDate", StatisticsData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<StatisticsData> result = new ArrayList<StatisticsData>();
        for (StatisticsData data : query.getResultList()) {
            result.add(data);
        }
        return result;
    }

    StatisticsDataFacade(EntityManager em) {
        super(StatisticsData.class);
        this.em = em;
    }
}
