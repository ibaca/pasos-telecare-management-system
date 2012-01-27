/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.devices;

import javax.ejb.Remote;

/**
 *
 * @author ibaca
 */
@Remote
public interface DeviceConnectorRemote {

CharSequence processAlertMessage(String from, String message);

public void createAlert(org.inftel.tms.domain.AlertType type, org.inftel.tms.domain.AlertPriority priority, java.lang.String cause, org.inftel.tms.domain.AlertRaw raw, java.lang.String mobileNumber);
  
}
