package org.inftel.tms.devices;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.soap.SOAPBinding;
import org.apache.commons.lang3.StringUtils;
import org.inftel.tms.domain.AlertRaw;
import org.inftel.tms.services.AlertFacadeRemote;
import org.inftel.tms.services.AlertRawFacadeRemote;

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

  @EJB
  private AlertFacadeRemote alertFacade;
  @EJB
  private AlertRawFacadeRemote alertRawFacade;
  private static final Logger logger = Logger.getLogger(DeviceConnector.class.getName());
  
  /*
   * remote parameters programming   
   */
  private static final String key = "&RK123456";
  private static final String call = "&RV1911234567";
  private static final String sms = "&RS1601234567";
  private static final String id = "&KO1000";
  private static final String transport = "&RT2:TCP";
  private static final String ip = "&RI01:12700000000108080";

    public DeviceConnector() {
    }

  /**
   * Comprueba si la trama recibida del terminal es una Alarma técnica
   *
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isTechnicalAlarm(String message) {
    Pattern pattern = null;
    return Pattern.matches("^\\*\\$AT.*$", message);
  }

  /**
   * Comprueba si la trama recibida del terminal es una Alarma del usuario
   *
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isUserAlarm(String message) {
    Pattern pattern = null;
    return Pattern.matches("^\\*\\$AU.*$", message);
  }

  /**
   * Comprueba si la trama recibida del terminal es una Alarma del dispositivo
   *
   * @param message el contenido del mensaje en formato paSOS
   * @return boolean
   */
  private boolean isDeviceAlarm(String message) {
    Pattern pattern = null;
    return Pattern.matches("^\\*\\$AD.*$", message);
  }
  
  private boolean isACK(String message) {
    boolean isStatusReport = Pattern.matches("^\\*\\$SR0.*$", message);
    
    Pattern pattern = Pattern.compile("\\&KO[0-9]{4}");
    Matcher matcher = pattern.matcher(message);    
    boolean isACK = matcher.find();
    return ( (isStatusReport) && (isACK) ) ;
  }
    
  private boolean checkKey(String message) {
    Pattern pattern = Pattern.compile("(\\&RK[0-9]{6})");
    Matcher matcher = pattern.matcher(message);    
    if (matcher.find()){
        return matcher.group(1).equals(key);        
    }
    else{
        return false;
    }
  }
  
  private boolean checkACK(String message) {
    Pattern pattern = Pattern.compile("(\\&KO[0-9]{4})");
    Matcher matcher = pattern.matcher(message);    
    if (matcher.find()){
        return matcher.group(1).equals(id);        
    }
    else{
        return false;
    }
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
    
    if (StringUtils.isBlank(from) || StringUtils.isBlank("message")) {
      throw new IllegalArgumentException("el origen y el contenido de una alerta no pueden ser nulos o cadenas vacias");
    }

// FIXME ANTES NO PETABA ESTO Y AHORA SI?    
//    AlertRaw raw = new AlertRaw();
//    raw.setOrigin(from);
//    raw.setRawData(message);
//    alertRawFacade.create(raw); 
    
    if ((message == null) || (message.isEmpty())) {
      logger.log(Level.INFO, "empty message received");
      return "*$RP06" + key + key + call + sms + id + transport + ip + "#";
    } 
    else if (isACK(message)) {      
      logger.log(Level.INFO, "ACK received");
      if (!checkKey(message))   throw new RuntimeException("ERROR, 'access key' incorrecta.");
      else if (!checkACK(message)) throw new RuntimeException("ERROR, codigo de 'ACK' incorrecto.");
      return null;
    }
    else if (isUserAlarm(message)) {
      //TODO crear alert y alertRaw con EJB
      logger.log(Level.INFO, "User Alarm received");
      if (!checkKey(message))   throw new RuntimeException("ERROR, 'access key' incorrecta.");
      return null;
    }
    else if (isDeviceAlarm(message)) {
      //TODO crear alert y alertRaw con EJB
      logger.log(Level.INFO, "Device Alarm received");
      if (!checkKey(message))   throw new RuntimeException("ERROR, 'access key' incorrecta.");
      return null;
    } 
    else if (isTechnicalAlarm(message)) {
      //TODO crear alert y alertRaw con EJB
      logger.log(Level.INFO, "Technical Alarm received");
      if (!checkKey(message))   throw new RuntimeException("ERROR, 'access key' incorrecta.");
      return null;
    } 
    else {
      //TODO Error
      logger.log(Level.SEVERE, "ERROR: trama no soportada");
      throw new RuntimeException("ERROR: trama no soporteda");            
    }
    
  }

  // Internal Test Usage
  DeviceConnector(AlertFacadeRemote alertFacade, AlertRawFacadeRemote alertRawFacade) {
    this.alertFacade = alertFacade;
    this.alertRawFacade = alertRawFacade;
  }
}
