package org.inftel.tms.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import org.inftel.tms.domain.Intervention;
import org.inftel.tms.domain.User;

/**
 * 
 * @author ibaca
 */
@Remote
public interface InterventionFacadeRemote {

    void create(Intervention intervention);

    void edit(Intervention intervention);

    void remove(Intervention intervention);

    Intervention find(Object id);

    List<Intervention> findAll();

    List<Intervention> findRange(int[] range);

    int count();

    public abstract Long countByUserAndInterval(User user, Date from, Date to);

    public abstract Long countByUser(User user);
}
