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
public class StatisticDataFacade extends AbstractFacade<StatisticData> {

    @PersistenceContext(unitName = "tms-statistic-unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatisticDataFacade() {
        super(StatisticData.class);
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
    public int sumStatictics(String startWith, StatisticDataPeriod period, Date fromDate, Date toDate) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o WHERE o.name LIKE :name AND o.periodType = :period AND o.periodDate BETWEEN :fromDate AND :toDate", StatisticData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<StatisticData> result = query.getResultList();
        int sum = 0;
        for (StatisticData s : result) {
            // FIXME de nuevo, esto deberia ser BigDecimal
            sum += s.getDataValue().intValue();
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
    public Map<String, Long> findStatistics(String startWith, StatisticDataPeriod period, Date date) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o WHERE o.name LIKE :name AND o.periodType = :period AND o.periodDate = :date", StatisticData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("date", date);
        Map<String, Long> result = new TreeMap<String, Long>();
        for (StatisticData data : query.getResultList()) {
            // FIXME deberia devolverse BigDecimal
            result.put(data.getName(), data.getDataValue().longValue());
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
    public List<StatisticData> findStatistics(String startWith, StatisticDataPeriod period, Date fromDate, Date toDate) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o WHERE o.name LIKE :name AND o.periodType = :period AND o.periodDate BETWEEN :fromDate AND :toDate", StatisticData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("period", period);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<StatisticData> result = new ArrayList<StatisticData>();
        for (StatisticData data : query.getResultList()) {
            result.add(data);
        }
        return result;
    }

    StatisticDataFacade(EntityManager em) {
        super(StatisticData.class);
        this.em = em;
    }
}
