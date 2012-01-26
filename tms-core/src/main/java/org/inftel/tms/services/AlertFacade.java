package org.inftel.tms.services;

import static java.util.logging.Level.INFO;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.domain.Person;

/**
 * 
 * @author ibaca
 */
@Stateless
public class AlertFacade extends AbstractFacade<Alert> implements AlertFacadeRemote {

    private final static Logger log = Logger.getLogger(AlertFacade.class.getName());

    @PersistenceContext(unitName = "tms-persistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlertFacade() {
        super(Alert.class);
    }

    @Override
    public List<Alert> findActiveAlerts() {
        log.info("buscando alertas activas");
        List<Alert> result = em.createNamedQuery(Alert.FIND_ACTIVED, Alert.class).getResultList(); 
        return result;
    }

    @Override
    public List<Alert> findAlertsByAffected(Person affected) {
        log.log(INFO, "buscando alertas del afectado {0}", new Object[] { affected });
        TypedQuery<Alert> query = em.createNamedQuery(Alert.FIND_BY_AFFECTED, Alert.class);
        query.setParameter("affected", affected);
        return query.getResultList();
    }

    @Override
    public int countByType(AlertType type, Date fromDate, Date toDate) {
        TypedQuery<Long> query = em.createNamedQuery(Alert.COUNT_BY_TYPE, Long.class);
        query.setParameter("type", type);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.getSingleResult().intValue();
    }

    // Internal test usage
    AlertFacade(EntityManager em) {
        super(Alert.class);
        this.em = em;
    }

}
