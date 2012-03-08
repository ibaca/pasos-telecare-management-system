
package org.inftel.tms.devices;

import static java.util.logging.Level.INFO;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.AlertPriority;
import org.inftel.tms.domain.AlertRaw;
import org.inftel.tms.domain.AlertType;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.Person;
import org.inftel.tms.services.AlertFacade;
import org.inftel.tms.services.AlertRawFacade;
import org.inftel.tms.services.DeviceFacade;
import org.inftel.tms.statistics.StatisticProcessor;

/**
 * Se encarga de la comunicacion entre los dispositivos y el servidor. Su
 * principal funcion es la de procesar los mensajes y convertirlos en
 * {@link Properties} que contengan todos los datos de la alerta. Este EJB no
 * debe acceder a ningun EntityManager, estas funciones deben quedar delegadas a
 * los respectivos facades llamando al metodo persist. Lo ideal es que nada mas
 * recibir el mensaje, este se haga persistente como AlertRaw estado 'no
 * procesado'. Y que el resto del procesamiento se haga de forma asincrona.
 * Aunque esto no es obligatorio. Ademas, el proceso puede necesitar parsear el
 * mensaje para decidir como responder. La captura HTTP se realiza en
 * {@link org.inftel.tms.web.DeviceConnectorDelegatorServlet}.
 * 
 * @author migueqm
 */
@Stateless
public class DeviceConnectorImpl implements DeviceConnector {

    private static final Logger logger = Logger.getLogger(DeviceConnectorImpl.class.getName());

    @EJB
    private DeviceFacade deviceFacade;

    @EJB
    private AlertFacade alertFacade;

    @EJB
    private AlertRawFacade alertRawFacade;

    @EJB
    private StatisticProcessor statisticProcessor;

    private static final String key = "&RK123456";
    private static final String call = "&RV1911234567";
    private static final String sms = "&RS1601234567";
    private static final String id = "&KO1000";
    private static final String transport = "&RT2:TCP";
    private static final String ip = "&RI01:12700000000108080";

    /** Crea una nueva instancia de DeviceConnectorImpl */
    public DeviceConnectorImpl() {
    }

    /**
     * Comprueba si la trama recibida del terminal es una Alarma técnica
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean isTechnicalAlarm(String message) {
        return Pattern.matches("^\\*\\$AT.*$", message);
    }

    /**
     * Comprueba si la trama recibida del terminal es una Alarma del usuario
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean isUserAlarm(String message) {
        return Pattern.matches("^\\*\\$AU.*$", message);
    }

    /**
     * Comprueba si la trama recibida del terminal es una Alarma del dispositivo
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean isDeviceAlarm(String message) {
        return Pattern.matches("^\\*\\$AD.*$", message);
    }

    /**
     * Comprueba si la trama recibida del terminal es un mensaje Status
     * Report(SR) con un frame ACK(KO)
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean isACK(String message) {
        boolean isStatusReport = Pattern.matches("^\\*\\$SR0.*$", message);

        Pattern pattern = Pattern.compile("\\&KO[0-9]{4}");
        Matcher matcher = pattern.matcher(message);
        boolean isACK = matcher.find();
        return ((isStatusReport) && (isACK));
    }

    /**
     * Comprueba que la Access Key es correcta, comparándola con la que envío el
     * servidor en su primer mensaje.
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean checkKey(String message) {
        Pattern pattern = Pattern.compile("(\\&RK[0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1).equals(key);
        } else {
            return false;
        }
    }

    /**
     * Comprueba que el ACK que se manda es correcto, comparando el ID de la
     * trama (RP) con el ID que se quiere enviar en el mensaje envía.
     * 
     * @param message el contenido del mensaje en formato paSOS
     * @return boolean
     */
    private boolean checkACK(String message) {
        Pattern pattern = Pattern.compile("(\\&KO[0-9]{4})");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1).equals(id);
        } else {
            return false;
        }
    }

    /**
     * Recibe los mensajes desde el servlet. Si el mensage es vacio (cadena
     * vacia o null) se considera como si el servidor se estuviese conectando
     * con el dispositivo. Y por tanto podra enviar mensajes de configuracion
     * hacia este. Procesa mensajes vacíos, mensajes ACK, User Alarms, Device
     * Alarms, Technical Alarms y mensajes erróneos
     * 
     * @param from el origen del mensaje, normalmente el numero de movil de
     *            afectado
     * @param message el contenido del mensaje en formato paSOS
     * @return mensaje paSOS que debe devolverse al dispositivo
     */
    @Override
    public CharSequence processAlertMessage(String from, String message) {
        logger.log(Level.INFO, "procesando mensaje de {0}: {1}", new Object[] {
                from, message
        });

        if (StringUtils.isBlank(from)) {
            throw new IllegalArgumentException(
                    "el origen de una alerta no puede ser nulo o cadena vacia");
        }

        long time = System.currentTimeMillis();
        try {
            // Primero se guarda el mensaje tal cual llega
            AlertRaw raw = internalProcessRawAlert(from, message);

            // Una vez guardado, se intenta parsear y se contruye la respuesta
            return internalProcessAlert(from, message, raw);
        } finally {
            time = System.currentTimeMillis() - time;
            statisticProcessor.process("alert.reciverProcessTime", new Date(), new Double(time));
        }
    }

    private CharSequence internalProcessAlert(String from, String message, AlertRaw raw) {
        if ((message == null) || (message.isEmpty())) {
            logger.log(Level.INFO, "empty message received");
            return "*$RP06" + key + key + call + sms + id + transport + ip + "#";
        } else if (isACK(message)) {
            logger.log(Level.INFO, "ACK received");
            if (!checkKey(message)) {
                throw new RuntimeException("ERROR, 'access key' incorrecta.");
            } else if (!checkACK(message)) {
                throw new RuntimeException("ERROR, codigo de 'ACK' incorrecto.");
            } else {
                createAlert(AlertType.TECHNICAL, AlertPriority.INFO,
                        "Parametros de configuracion recibidos correctamente", raw, from);
            }
            return null;
        } else if (isUserAlarm(message)) {
            logger.log(Level.INFO, "User Alarm received");
            if (!checkKey(message)) {
                throw new RuntimeException("ERROR, 'access key' incorrecta.");
            }
            createAlert(AlertType.USER, AlertPriority.CRITICAL, "Alerta de usuario", raw, from);
            return null;
        } else if (isDeviceAlarm(message)) {
            logger.log(Level.INFO, "Device Alarm received");
            if (!checkKey(message)) {
                throw new RuntimeException("ERROR, 'access key' incorrecta.");
            }
            createAlert(AlertType.DEVICE, AlertPriority.IMPORTANT, "Alerta dispositivo", raw,
                    from);
            return null;
        } else if (isTechnicalAlarm(message)) {
            logger.log(Level.INFO, "Technical Alarm received");
            if (!checkKey(message)) {
                throw new RuntimeException("ERROR, 'access key' incorrecta.");
            }
            createAlert(AlertType.TECHNICAL, AlertPriority.NORMAL, "Alerta tecnica", raw,
                    from);
            return null;
        } else {
            logger.log(Level.SEVERE, "ERROR: trama no soportada");
            throw new RuntimeException("ERROR: trama no soporteda");
        }
    }

    private AlertRaw internalProcessRawAlert(String from, String message) {
        AlertRaw raw = new AlertRaw();
        raw.setOrigin(from);
        raw.setRawData(message);
        raw.setCreated(new Date());
        alertRawFacade.create(raw);
        return raw;
    }

    private static Pattern latPattern = Pattern.compile("&LT([-]?\\d+[.\\d]*)");
    private static Pattern lngPattern = Pattern.compile("&LN([-]?\\d+[.\\d]*)");
    private static Pattern causePattern = Pattern.compile("&XCU([^&#]+)");

    /**
     * Crea una alerta con su alertRaw, persistentes en bbdd
     * 
     * @param type
     * @param priority
     * @param cause
     * @param raw
     * @param mobileNumber
     */
    public void createAlert(AlertType type, AlertPriority priority, String cause, AlertRaw raw,
            String mobileNumber) {
        Device device = deviceFacade.findByMobile(mobileNumber);
        if (device == null) {
            logger.log(INFO, "el movil {0} no esta registrado, no se procesará la alarma",
                    new Object[] {
                        mobileNumber
                    });
            return;
        } else {
            logger.log(INFO, "registrando alerta en el movil {0}", new Object[] {
                    device
            });
        }

        Person person = device.getOwner().getData();
        logger.log(INFO, "registrando alerta para la persona {0}", new Object[] {
                person
        });

        Alert alert = new Alert();
        alert.setAffected(person);
        alert.setCause(cause);
        alert.setOrigin(device);
        alert.setPriority(priority);
        alert.setType(type);
        alert.setRaw(raw);

        // Latitude & Longitude
        Matcher match;
        if ((match = latPattern.matcher(raw.getRawData())).find()) {
            alert.setLatitude(Double.parseDouble(match.group(1)));
        }
        if ((match = lngPattern.matcher(raw.getRawData())).find()) {
            alert.setLongitude(Double.parseDouble(match.group(1)));
        }

        // Custom cause
        if ((match = causePattern.matcher(raw.getRawData())).find()) {
            alert.setCause(match.group(1));
        }

        // Save alert and assign raw
        alertFacade.create(alert);
        raw.setAlert(alert);
    }

    // Internal Test Usage
    DeviceConnectorImpl(AlertFacade alertFacade, AlertRawFacade alertRawFacade,
            DeviceFacade deviceFacade, StatisticProcessor statisticProcessor) {
        this.alertFacade = alertFacade;
        this.alertRawFacade = alertRawFacade;
        this.deviceFacade = deviceFacade;
        this.statisticProcessor = statisticProcessor;
    }
}
