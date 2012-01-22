/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import java.util.List;
import javax.ejb.Remote;
import org.inftel.tms.domain.Device;

/**
 *
 * @author ibaca
 */
@Remote
public interface DeviceFacadeRemote {

  void create(Device device);

  void edit(Device device);

  void remove(Device device);

  Device find(Object id);

  List<Device> findAll();

  List<Device> findRange(int[] range);

  int count();
  
}
