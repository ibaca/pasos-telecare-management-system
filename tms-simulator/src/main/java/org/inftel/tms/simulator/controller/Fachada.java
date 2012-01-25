package org.inftel.tms.simulator.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.inftel.tms.services.UserFacadeRemote;
import org.inftel.tms.simulator.model.Parameters;


/**
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

  public static final String USER_FACADE_JNDI = "java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/tms-core-1.0-SNAPSHOT/UserFacade!org.inftel.tms.services.UserFacadeRemote";  
  private Parameters parameters;
  private String senderMobileNumber;
    
  public Fachada() {
        
  }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    
    public void setParameters(String trama) {
        this.parameters = parseTrama(trama);
    }

  public String getSenderMobileNumber() {
      return senderMobileNumber;
  }

  public void setSenderMobileNumber(String number) {
      this.senderMobileNumber = number;
  }

  //Manda un mensaje vacío al servlet para solicitar los Remote Parameters
  public HttpResponse sendEmptyMessage() throws URISyntaxException, IOException{    
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", senderMobileNumber);
        URI uri = new URI("http://localhost:8080/tms-web/connector");
        post.setURI(uri);
        return client.execute(post);
  }  
  
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

    private Parameters parseTrama(String trama) {
        //*$RP06 &RK123456 &RK123456 &RV1911234567 &KO1000 &RT2:TCP &RI01:12700000000108080#
        Parameters param = new Parameters();
        StringTokenizer tokens = new StringTokenizer(trama.trim(), "&#*$");        
    
        //$RP06 RK123456 RV1911234567 RS1601234567 KO1000 RT2:TCP RI01:12700000000108080        
        while(tokens.hasMoreTokens()){
            String token = tokens.nextToken();
            if(token.contains("RK"))        param.setKey(token);
            else if (token.contains("RV"))  param.setCall(token);
            else if (token.contains("KO"))  param.setId(token);
            else if (token.contains("RT"))  param.setTransport(token);
            else if (token.contains("RI"))  param.setIp(token);
            else if (token.contains("RS"))  param.setSms(token);
            else if (token.contains("RP"))  {;}
            else throw new RuntimeException("Trama incorrecta: "+ token);
        }        
        return param; 
    }

    public HttpResponse enviarACK() throws URISyntaxException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", senderMobileNumber);
        URI uri = new URI("http://localhost:8080/tms-web/connector");
        post.setURI(uri);
        post.setEntity(new StringEntity("*$SR0&"+parameters.getId().substring(2, 5)));
        return client.execute(post);
    }

    public HttpResponse enviarUserAlarm() throws URISyntaxException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", senderMobileNumber);
        URI uri = new URI("http://localhost:8080/tms-web/connector");
        post.setURI(uri);
        post.setEntity(new StringEntity("*$AU11&"
                +parameters.getKey()
                +"&LD20160303"
                +"&LH060654"
                +"&LN1008052067"
                +"&LT153052067"
                +"#"));
        return client.execute(post);        
    }
    
    //$AD31&LD20160303&LH060654&LN1008052067&LT153052067&DT75#
    public HttpResponse enviarDeviceAlarm() throws URISyntaxException, UnsupportedEncodingException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", senderMobileNumber);
        URI uri = new URI("http://localhost:8080/tms-web/connector");
        post.setURI(uri);
        post.setEntity(new StringEntity("*$AD31&"
                +parameters.getKey()
                +"&LD20160303"
                +"&LH060654"
                +"&LN1008052067"
                +"&LT153052067"
                +"&DT75#"));
        return client.execute(post);  
    }
}
