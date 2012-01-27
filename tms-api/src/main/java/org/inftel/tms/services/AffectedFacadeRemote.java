package org.inftel.tms.services;

import java.util.List;

import javax.ejb.Local;

import org.inftel.tms.domain.Affected;
import org.inftel.tms.domain.AffectedType;

/**
 * 
 * @author ibaca
 */
@Local
public interface AffectedFacadeRemote {

    void create(Affected affected);

    void edit(Affected affected);

    void remove(Affected affected);

    Affected find(Object id);

    List<Affected> findAll();

    List<Affected> findRange(int[] range);

    int count();

    Long countByType(AffectedType type);
}
