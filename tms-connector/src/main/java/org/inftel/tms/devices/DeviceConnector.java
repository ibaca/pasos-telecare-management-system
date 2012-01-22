package org.inftel.tms.devices;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 * Se encarga de la comunicacion entre los dispositivos y el servidor. Su principal funcion es la de
 * procesar los mensajes y convertirlos en {@link Properties} que contengan todos los datos de la
 * alerta.
 *
 * Este EJB no debe acceder a ningun EntityManager, estas funciones deben quedar delegadas a los
 * respectivos facades llamando al metodo persist. Lo ideal es que nada mas recibir el mensaje, este
 * se haga persistente como AlertRaw estdo 'no procesado'. Y que el resto del procesamiento se haga
 * de forma asincrona. Aunque esto no es obligatorio. Ademas, el proceso puede necesitar parsear el
 * mensaje para decidir como responder.
 *
 * @author ibaca
 */
@Stateless
public class DeviceConnector implements DeviceConnectorRemote {

  private static final Logger logger = Logger.getLogger(DeviceConnector.class.getName());

  /**
   * Recibe los mensajes desde el servlet.
   *
   * Si el mensage es vacio (cadena vacia o null) se considera como si el servidor se estuviese
   * conectando con el dispositivo. Y por tanto podra enviar mensajes de configuracion hacia este.
   *
   * @param from el origen del mensaje, normalmente el numero de movil de afectado
   * @param message el contenido del mensaje en formato paSOS
   * @return mensaje paSOS que debe devolverse al dispositivo
   */
  @Override
  public CharSequence processAlertMessage(String from, String message) {
    logger.log(Level.INFO, "procesando mensaje de {0}: {1}", new Object[]{from, message});
    return null;
  }
}
