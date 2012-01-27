package org.inftel.tms.services;

import java.util.List;

import javax.ejb.Local;

import org.inftel.tms.domain.AlertRaw;

/**
 * 
 * @author ibaca
 */
@Local
public interface AlertRawFacadeRemote {

    void create(AlertRaw alertsRaw);

    void edit(AlertRaw alertsRaw);

    void remove(AlertRaw alertsRaw);

    AlertRaw find(Object id);

    List<AlertRaw> findAll();

    List<AlertRaw> findRange(int[] range);

    int count();
}
