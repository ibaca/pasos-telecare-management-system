package org.inftel.tms.simulator;

import javax.naming.Context;
import javax.naming.InitialContext;
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
public class Main {

  public static final String USER_FACADE_JNDI = "java:global/org.inftel.tms_tms-bundle_ear_1.0-SNAPSHOT/tms-core-1.0-SNAPSHOT/UserFacade!org.inftel.tms.services.UserFacadeRemote";

  public static void main(String[] args) throws Exception {
    Context context = new InitialContext();
    System.out.println("Hello World Enterprise Application Client!");
    UserFacadeRemote userService = (UserFacadeRemote) context.lookup(USER_FACADE_JNDI);
    System.out.println("remote user service count response: " + userService.count());
  }
}
