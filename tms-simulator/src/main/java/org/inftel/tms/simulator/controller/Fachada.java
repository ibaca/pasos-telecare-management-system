package org.inftel.tms.simulator.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.inftel.tms.services.UserFacadeRemote;
import org.inftel.tms.simulator.model.Parameters;


/**
 * @author migueqm
 * Enterprise Application Client main class.
 *
 * Se muestra un ejemplo de obtener el interfaz remoto de {@link UserFacadeRemote} y llamar a uno de
 * sus metodos. Podria ser buena idea que los nombres JNDI estandard estuviesen en el API como
 * variables estaticas.
 *
 * Para poder ejecutar este ejemplo debe de desplegarse primero la aplicacion en el contenedor
 * glassfish. La aplicacion se despliega a traves del proyecto tms-bundle.
 *
 */
public class Fachada {

    /**
     * JNDI de USER_FACADE
     */
    public static final String USER_FACADE_JNDI = "java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/tms-core-1.0-SNAPSHOT/UserFacade!org.inftel.tms.services.UserFacadeRemote";  
    /**
     * Parametros de la trama
     */
    protected Parameters parameters;
    
    /**
     * Constructor de clase
     */
    public Fachada() {
        parameters = new Parameters();
  }

  
  /**
   * Devuelve los parametros
   * @return Parameters
   */
  public Parameters getParameters() {
        return parameters;
    }

  /**
   * Establece los parametros
   * @param parameters
   */
  public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    
  /**
   * Establece los parametros a partir de la trama recibida de la centralita (remote parameters programming)
   * @param trama
   */
  public void setParameters(String trama) {
        parseTrama(trama);
    }
  
    /**
     * Manda un mensaje vacío, para pedir los remote parameters.
     * Segun el protocolo PASOS, es la centralita quien manda el primer mensaje 
     * por SMS al terminal con los parámetros.
     * Nosotros lo simulamos pidiendole los parámetros a la centralita con un 
     * mensaje vacío.Siempre incluimos en la cabecera HTTP el numero de movil que origina la trama.
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResponse sendEmptyMessage() throws URISyntaxException, IOException{    
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", parameters.getSenderMobileNumber());
        URI uri = new URI(parameters.getURLservlet());
        post.setURI(uri);
        return client.execute(post);
  }  
  
  /**
   * Convierte un InputStream a String
   * @param in
   * @return
   */
  public String readStreamAsString(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int count;
            do {
                count = in.read(buffer);
                if (count > 0) {
                    out.write(buffer, 0, count);
                }
            } while (count >= 0);
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("La maquina JVM no soporta codificación UTF-8", e);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }
  
    // Parsea una trama y guarda sus parametros en el atributo Parameters de la fachada
    private void parseTrama(String trama) {
        StringTokenizer tokens = new StringTokenizer(trama.trim(), "&#*$");        
    
        //$RP06 RK123456 RV1911234567 RS1601234567 KO1000 RT2:TCP RI01:12700000000108080        
        while(tokens.hasMoreTokens()){
            String token = tokens.nextToken();
            if(token.contains("RK"))        parameters.setKey(token);
            else if (token.contains("RV"))  parameters.setCall(token);
            else if (token.contains("KO"))  parameters.setId(token);
            else if (token.contains("RT"))  parameters.setTransport(token);
            else if (token.contains("RI"))  parameters.setIp(token);
            else if (token.contains("RS"))  parameters.setSms(token);
            else if (token.contains("RP"))  {;}
            else throw new RuntimeException("Trama incorrecta: "+ token);
        }               
    }

    /**
     * Envia un ACK al servidor, con el ID de la trama recibida anteriormente: "remote parameters 
     * programming" para decirle al servidor que hemos recibido los parametros 
     * de configuracion correctamente.
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResponse enviarACK() throws URISyntaxException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", parameters.getSenderMobileNumber());
        URI uri = new URI(parameters.getURLservlet());
        post.setURI(uri);
        post.setEntity(new StringEntity("*$SR0&"+parameters.getKey()+"&"+parameters.getId()+"#"));
        return client.execute(post);
    }

    /**
     * Envia una alerta de usuario (AU), usamos una latitud y longitud al azar, al no ser un móvil real
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResponse enviarUserAlarm() throws URISyntaxException, IOException {                
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", parameters.getSenderMobileNumber());
        URI uri = new URI(parameters.getURLservlet());
        post.setURI(uri);
        parameters.setDateandTime();
        post.setEntity(new StringEntity("*$AU11&"
                +parameters.getKey()
                +parameters.getDate()
                +parameters.getTime()
                +"&LN1008052067"
                +"&LT153052067"
                +"#"));
        return client.execute(post);        
    }
    
    /**
     * Envia una alerta de dispositivo (AD), usamos una latitud y longitud al azar, 
     * al no ser un móvil real. Simulamos una alerta por temperatura.
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public HttpResponse enviarDeviceAlarm() throws URISyntaxException, UnsupportedEncodingException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", parameters.getSenderMobileNumber());
        URI uri = new URI(parameters.getURLservlet());
        post.setURI(uri);
        parameters.setDateandTime();
        post.setEntity(new StringEntity("*$AD31&"
                +parameters.getKey()
                +parameters.getDate()
                +parameters.getTime()
                +"&LN1008052067"
                +"&LT153052067"
                +"&DT"+parameters.getTemperature()+"#"));
        return client.execute(post);  
    }
    
    /**
     * Envia una alarma técnica (AT), usamos una latitud y longitud al azar, 
     * al no ser un móvil real.Simulamos una alerta por el nivel de batería, 
     * indicando además que el terminal no está conectado al cargador.
     * @return HttpRespose
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public HttpResponse enviarTechnicalAlarm() throws URISyntaxException, UnsupportedEncodingException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", parameters.getSenderMobileNumber());
        URI uri = new URI(parameters.getURLservlet());
        post.setURI(uri);
        parameters.setDateandTime();
        post.setEntity(new StringEntity("*$AT2&"
                +parameters.getKey()
                +parameters.getDate()
                +parameters.getTime()
                +"&LN1008052067"
                +"&LT153052067"
                +"&PB"+parameters.getBattery()
                +"PC000#"));
        return client.execute(post);  
    }
    
}
