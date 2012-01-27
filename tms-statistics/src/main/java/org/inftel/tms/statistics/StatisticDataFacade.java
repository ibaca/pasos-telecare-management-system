package org.inftel.tms.statistics;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
     * Suma las estadísticas diarias, mensuales o anuales de un determinado valor entre un rango de
     * fechas
     * 
     * @param startWith
     *            nombre del atributo estadístico
     * @param period
     *            diario, mensual o anual
     * @param fromDate
     *            fecha de inicio del sumatorio
     * @param toDate
     *            fecha fin del sumatorio
     * @return sumatorio de los estadísticos
     */
    public int sumStatictics(String startWith, StatisticDataPeriod period, Date fromDate,
            Date toDate) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o "
                + "WHERE o.name LIKE :name AND o.periodType = :period "
                + "AND o.periodDate BETWEEN :fromDate AND :toDate", StatisticData.class);
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
     * Busca todos los valores estadisticos que comiencen con el nombre pasado como parametro.
     * 
     * @param startWith
     *            nombre estadistico que filtra los tipos que se quieren obtener
     * @param period
     *            periodo estadistico que se quiere obtener
     * @param date
     *            fecha del periodo que se quiere obtener
     * @return parejas de nombre de estadistica y valor para el periodo y facha pasados
     */
    public Map<String, Long> findStatistics(String startWith, StatisticDataPeriod period, Date date) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o "
                + "WHERE o.name LIKE :name AND o.periodType = :period AND o.periodDate = :date",
                StatisticData.class);
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
     * Busca el intervalo de datos estadisticos para los nombres de estadisticas que comiencen con
     * el nombre pasado como parametro en un rango de fechas.
     * 
     * Para facilitar las busquedas, este metodo filtra las fechas pasadas para ajustarlas al
     * periodo, de esta forma si por ejemplo se pide periodo anual, y la fecha es desde el 3 enero
     * 2011 hasta 7 marzo del 2011, se devolvera la muestra anual del 2011.
     * 
     * @param name
     *            nombre del estadistico que se quieren obtener (i.e. alert.type.user)
     * @param period
     *            periodo estadistico que se quiere obtener [DAILY|MOUNTHLY|ANNUAL]
     * @param fromDate
     *            inicio de intervalo
     * @param toDate
     *            fin de intervalo
     * @return
     */
    public Map<Date, Long> findStatistics(String name, StatisticDataPeriod period, Date fromDate,
            Date toDate) {
        fromDate = period.beginsAt(fromDate);
        toDate = period.endsAt(toDate);

        TypedQuery<StatisticData> query = em.createQuery("SELECT o "
                + "FROM StatisticData o WHERE o.name = :name AND o.periodType = :period "
                + "AND o.periodDate " + "BETWEEN :fromDate AND :toDate", StatisticData.class);
        query.setParameter("name", name);
        query.setParameter("period", period);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        Map<Date, Long> result = new TreeMap<Date, Long>();
        for (StatisticData data : query.getResultList()) {
            result.put(data.getPeriodDate(), data.getDataValue().longValue());
        }
        return result;
    }

    public StatisticData findByDate(String startWith, Date date) {
        TypedQuery<StatisticData> query = em.createQuery("SELECT o FROM StatisticData o "
                + "WHERE o.name LIKE :name AND o.periodDate = :date", StatisticData.class);
        query.setParameter("name", startWith + "%");
        query.setParameter("date", date);
        query.setMaxResults(1); // aunq el modelo no debe devolver mas de uno

        List<StatisticData> result = query.getResultList();
        return (result.size() > 0) ? result.get(0) : null;
    }

    public List<String> findStatisticsNames(String startWith) {
        TypedQuery<String> query = em.createQuery("SELECT DISTINCT o.name "
                + "FROM StatisticData o WHERE o.name LIKE :name", String.class);
        return query.setParameter("name", startWith + "%").getResultList();
    }

    StatisticDataFacade(EntityManager em) {
        super(StatisticData.class);
        this.em = em;
    }
}
