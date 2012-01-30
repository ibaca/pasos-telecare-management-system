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
public class AffectedFacadeImpl extends AbstractFacade<Affected> implements AffectedFacade {

    @PersistenceContext(unitName = "tms-persistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AffectedFacadeImpl() {
        super(Affected.class);
    }

    public Long countByType(AffectedType type) {
        TypedQuery<Long> query = em.createNamedQuery("Affected.countByType", Long.class);
        query.setParameter("type", type);
        return query.getSingleResult();
    }
}
