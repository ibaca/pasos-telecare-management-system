package org.inftel.tms.simulator.model;

import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

/**
 * Parametros
 * @author migueqm
 */
public class Parameters {
  private String key = "";
  private String call = "";
  private String sms = "";
  private String id = "";
  private String transport = "";
  private String ip = "";
  private String date="";
  private String time="";
  private String URLservlet="";
  private String senderMobileNumber="";
  private String temperature;
  private String battery;

  /**
   * Devuelve la URL del servidor
   * @return String
   */
  public String getURLservlet() {
        return URLservlet;
    }

  /**
   * Establece la URL del servidor
   * @param URLservlet
   */
  public void setURLservlet(String URLservlet) {
        this.URLservlet = URLservlet;
    }

  /**
   * Devuelve el nivel de batería
   * @return String
   */
  public String getBattery() {
        return battery;
    }

  /**
   * Establece el nivel de batería
   * @param battery
   */
  public void setBattery(String battery) {
        this.battery = StringUtils.leftPad(battery, 2, '0');
    }

  /**
   * Devuelve el número de teléfono móvil
   * @return String
   */
  public String getSenderMobileNumber() {
        return senderMobileNumber;
    }

  /**
   * Establece el número de teléfono móvil
   * @param senderMobileNumber
   */
  public void setSenderMobileNumber(String senderMobileNumber) {
        this.senderMobileNumber = senderMobileNumber;
    }

  /**
   * Devuelve la temperatura del terminal
   * @return String
   */
  public String getTemperature() {
        return temperature;
    }

  /**
   * Establece la temperatura del terminal
   * @param temperature
   */
  public void setTemperature(String temperature) {
        this.temperature = StringUtils.leftPad(temperature, 2, '0');
    }
  
  
  /**
   * Constructor de la clase
   */
  public Parameters() {
                              
    }

  /**
   * Establece las tramas "Data" y "Time" en el formato que indica el protocolo PASOS.
   * Les asigna la fecha y hora actuales.
   */
  public void setDateandTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendar.get(Calendar.MINUTE);
        int seconds=calendar.get(Calendar.SECOND);
        String yearString="";
        String dayString="";
        String monthString="";
        String hourString="";
        String minsString="";
        String secsString="";
        
        yearString = String.valueOf(year);
        if(day<10)      dayString = String.format("%02d",day);
            else        dayString = String.valueOf(day);
        
        if(month<10)    monthString = String.format("%02d",month);
            else        monthString = String.valueOf(month);
        
        if(hour<10)     hourString = String.format("%02d",hour);
            else        hourString = String.valueOf(hour);
        
        if(minutes<10)  minsString = String.format("%02d",minutes);
            else        minsString =  String.valueOf(minutes);
        if(seconds<10)  secsString = String.format("%02d",seconds);
            else        secsString =  String.valueOf(seconds);
                
        this.date = "&LD" + yearString + monthString + dayString;
        this.time = "&LH" + hourString + minsString + secsString;
    }
    
    /**
     * Devuelve la trama Date
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Establece la trama Date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Devuelve la trama Time
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * Establece la trama Time
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Devuelve el número de teléfono de la centralita
     * @return String
     */
    public String getCall() {
        return call;
    }

    /**
     * Establece el número de teléfono de la centralita
     * @param call
     */
    public void setCall(String call) {
        this.call = call;
    }

    /**
     * Devuelve el ID de la última trama recibida, para poder enviarle un mensaje 
     * ACK al servidor posteriormente
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el Identificador de la trama, usado para enviarle un mensaje ACK
     * al servidor.
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el número de la central, la direccion IP, el protocolo y el puerto, 
     * en el formato indicado por el protocolo PASOS.(Nosotros usamos por defecto 
     * TCP para conexiones tanto entrantes como salientes)
     * @return String
     */
    public String getIp() {
        return ip;
    }

    /**
     * Establece el número de la central, la dirección IP, y el puerto, en el formato 
     * indicado por el protocolo PASOS.
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Devuelve el Access Key, usada para identificarse con el servidor.
     * @return String
     */
    public String getKey() {
        return key;
    }

    /**
     * Establece la Access Key
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Devuelve el número de teléfono que la central usa para comunicarse por
     * mensajes SMS.
     * @return String
     */
    public String getSms() {
        return sms;
    }

    /**
     * Establece el número de teléfono que la central usa para comunicarse por 
     * mensajes SMS.
     * @param sms
     */
    public void setSms(String sms) {
        this.sms = sms;
    }

    /**
     * Devuelve el protocolo que se usa en las comunicaciones con el servidor, 
     * en formato PASOS.
     * @return String
     */
    public String getTransport() {
        return transport;
    }

    /**
     * Establece el protocolo que se usa en las comunicaciones con el servidor, 
     * en formato PASOS.
     * @param transport
     */
    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Parameters{" + "key=" + key + ", call=" + call + ", sms=" + sms + ", id=" + id + ", transport=" + transport + ", ip=" + ip + ", date=" + date + ", time=" + time + ", URLservlet=" + URLservlet + ", senderMobileNumber=" + senderMobileNumber + ", temperature=" + temperature + ", battery=" + battery + '}';
    }

    


}
