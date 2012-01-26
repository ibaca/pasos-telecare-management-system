package org.inftel.tms.services;

import static org.inftel.tms.domain.Device.FIND_BY_MOBILE;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.inftel.tms.domain.Device;

/**
 * 
 * @author ibaca
 */
@Stateless
public class DeviceFacade extends AbstractFacade<Device> implements DeviceFacadeRemote {

    @PersistenceContext(unitName = "tms-persistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeviceFacade() {
        super(Device.class);
    }

    public Device findByMobile(String mobile) {
        TypedQuery<Device> query = em.createNamedQuery(FIND_BY_MOBILE, Device.class);
        query.setParameter("mobile", mobile);
        return query.getSingleResult();
    }
}
