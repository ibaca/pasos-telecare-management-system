package org.inftel.tms.statistics;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Se encarga de procesar la cola de estadistiacs e ir acutalizando la tabla de estadistica. El pool
 * de beans esta configurado para tener un maximo de 1, por lo tanto no hay problema de actualizar
 * las filas de la tabla. Ademas, el MessageDriven es no transaccional, y se recomienda usar acceso
 * a las tablas no transaccional.
 */
@MessageDriven(mappedName = "jms/statistics", activationConfig = {
  @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
  @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ProcessableStatisticMessage implements MessageListener {

  public ProcessableStatisticMessage() {
  }

  @Override
  public void onMessage(Message message) {
  }
}
