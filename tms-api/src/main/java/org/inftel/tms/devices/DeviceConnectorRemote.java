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
  
}
