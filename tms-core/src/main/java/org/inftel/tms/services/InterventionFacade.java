package org.inftel.tms.services;

import static org.inftel.tms.domain.Intervention.COUNT_BY_TYPE;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.inftel.tms.domain.Intervention;
import org.inftel.tms.domain.User;

/**
 * 
 * @author ibaca
 */
@Stateless
public class InterventionFacade extends AbstractFacade<Intervention> implements
        InterventionFacadeRemote {

    @PersistenceContext(unitName = "tms-persistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InterventionFacade() {
        super(Intervention.class);
    }

    @Override
    public Long countByUser(User user) {
        return countByUserAndInterval(user, new Date(0), new Date());
    }

    @Override
    public Long countByUserAndInterval(User user, Date from, Date to) {
        TypedQuery<Long> query = em.createNamedQuery(COUNT_BY_TYPE, Long.class);
        query.setParameter("fromDate", from).setParameter("toDate", to);
        return query.setParameter("user", user).getSingleResult();
    }
}
