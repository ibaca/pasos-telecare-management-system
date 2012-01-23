package org.inftel.tms.devices;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.security.auth.login.Configuration;

/**
 * Se encarga de la comunicacion entre los dispositivos y el servidor. Su principal funcion es la de
 * procesar los mensajes y convertirlos en {@link Properties} que contengan todos los datos de la
 * alerta.
 *
 * Este EJB no debe acceder a ningun EntityManager, estas funciones deben quedar delegadas a los
 * respectivos facades llamando al metodo persist. Lo ideal es que nada mas recibir el mensaje, este
 * se haga persistente como AlertRaw estado 'no procesado'. Y que el resto del procesamiento se haga
 * de forma asincrona. Aunque esto no es obligatorio. Ademas, el proceso puede necesitar parsear el
 * mensaje para decidir como responder.
 * 
 * La captura HTTP se realiza en {@link org.inftel.tms.web.DeviceConnectorDelegatorServlet}.
 *
 * @author ibaca
 */
@Stateless
public class DeviceConnector implements DeviceConnectorRemote {

  private static final Logger logger = Logger.getLogger(DeviceConnector.class.getName());
  
  private Properties serverProps = new Properties();
  private Properties alertProps = new Properties();

  /**
   * Comprueba si la trama recibida del terminal es una Alarma técnica   
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isTechnicalAlarm(String message) {
        Pattern pattern = null;
        return Pattern.matches("^$AT",message);
    }

    /**
   * Comprueba si la trama recibida del terminal es una Alarma del usuario   
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isUserAlarm(String message) {
        Pattern pattern = null;
        return Pattern.matches("^$AU",message);
    }
    
    /**
   * Comprueba si la trama recibida del terminal es una Alarma del dispositivo   
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isDeviceAlarm(String message) {
        Pattern pattern = null;
        return Pattern.matches("^$AD",message);
    }
  
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
    
    try {
       serverProps.load(Configuration.class.getClassLoader().getResourceAsStream("server.properties"));
    } catch (IOException ex) {
        
    }
    //remote parameters programming
    if( (message == null) || (message.isEmpty()) ){
        return "*$RP06"+
                serverProps.getProperty("key") 
                +serverProps.getProperty("key")
                +serverProps.getProperty("call")
                +serverProps.getProperty("sms")
                +serverProps.getProperty("id")
                +serverProps.getProperty("transport")
                +serverProps.getProperty("ip")
                +"#";
    }
    else if (isTechnicalAlarm(message)){
        //System.out.println("ERROR!");
        return null;
    }    
    else if (isUserAlarm(message)){
        //crear alert y alertRaw con ejb
        return null;
    }    
    else if (isDeviceAlarm(message)){
        //crear alert y alertRaw con ejb
        return null;
    }    
    else{
        return null;
    }
  }


}
