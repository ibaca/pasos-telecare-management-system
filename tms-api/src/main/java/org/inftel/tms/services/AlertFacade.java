package org.inftel.tms.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.domain.Person;

/**
 * 
 * @author ibaca
 */
@Local
public interface AlertFacade {

    void create(Alert alerts);

    void edit(Alert alerts);

    void remove(Alert alerts);

    Alert find(Object id);

    List<Alert> findAll();

    List<Alert> findRange(int[] range);

    int count();

    List<Alert> findActiveAlerts();

    List<Alert> findAlertsByAffected(Person affected);

    Long countByType(AlertType type, Date fromDate, Date toDate);
}
