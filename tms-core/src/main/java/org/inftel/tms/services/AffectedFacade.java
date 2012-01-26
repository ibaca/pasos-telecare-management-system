package org.inftel.tms.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.inftel.tms.domain.Affected;
import org.inftel.tms.domain.AffectedType;

/**
 * 
 * @author ibaca
 */
@Stateless
public class AffectedFacade extends AbstractFacade<Affected> implements AffectedFacadeRemote {

    @PersistenceContext(unitName = "tms-persistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AffectedFacade() {
        super(Affected.class);
    }

    public int countByType(AffectedType type) {
        TypedQuery<Long> query = em.createNamedQuery("Affected.countByType", Long.class);
        query.setParameter("type", type);
        return query.getSingleResult().intValue();
    }
}
