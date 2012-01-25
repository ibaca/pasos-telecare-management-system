package org.inftel.tms.statistics;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
