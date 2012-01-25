package org.inftel.tms.simulator.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.inftel.tms.services.UserFacadeRemote;


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

    public Fachada() {
    }

    //Prueba mandar mensaje vacío al servlet
  public HttpResponse sendEmptyMessage() throws URISyntaxException, IOException{    
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost();    
    post.setHeader("sender-mobile-number", "666123456");
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
  
  
}
