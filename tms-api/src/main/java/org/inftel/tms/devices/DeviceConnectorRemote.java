package org.inftel.tms.devices;

import javax.ejb.Local;

/**
 *
 * @author ibaca
 */
@Local
public interface DeviceConnectorRemote {

  CharSequence processAlertMessage(String from, String message);
  
}
