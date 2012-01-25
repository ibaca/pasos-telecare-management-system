package org.inftel.tms.statistics;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
   * Busca todos los valores estadisticos que comiencen con el nombre pasado como parametro.
   *
   * @param startWith nombre estadistico que filtra los tipos que se quieren obtener
   * @param period periodo estadistico que se quiere obtener
   * @param date fecha del periodo que se quiere obtener
   * @return parejas de nombre de estadistica y valor para el periodo y facha pasados
   */
  public Map<String, Long> findStatistics(String startWith, StatisticsData.statisticPeriod period, Date date) {
    TypedQuery<StatisticsData> query = em.createQuery("SELECT o FROM StatisticsData o WHERE o.name LIKE :name AND o.dataPeriod = :period AND o.dataDate = :date", StatisticsData.class);
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
   * Busca el intervalo de datos estadisticos para los nombres de estadisticas que comiencen con el
   * nombre pasado como parametro.
   *
   * @param startWith nombre estadistico que filtra los tipos que se quieren obtener
   * @param period periodo estadistico que se quiere obtener
   * @param fromDate inicio de intervalo
   * @param toDate fin de intervalo
   * @return
   */
  public Map<String, Map<Date, Long>> findStatistics(String startWith, StatisticsData.statisticPeriod period, Date fromDate, Date toDate) {
    return Collections.emptyMap();
  }

  StatisticsDataFacade(EntityManager em) {
    super(StatisticsData.class);
    this.em = em;
  }
}
