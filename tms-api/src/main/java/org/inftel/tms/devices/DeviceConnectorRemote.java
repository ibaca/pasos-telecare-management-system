package org.inftel.tms.devices;

import javax.ejb.Remote;

/**
 *
 * @author ibaca
 */
@Remote
public interface DeviceConnectorRemote {

  CharSequence processAlertMessage(String from, String message);
  
}
