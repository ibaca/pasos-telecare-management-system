package org.inftel.tms.simulator;

import java.util.Properties;
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
    System.out.println("Hello World Enterprise Application Client!");

    // Configuraciones, ahora no hace falta porque por defecto busca en localhost! pero cuando
    // se usa en diferentes maquinas el host debe ser un parametro configurable
    // mas info en: http://docs.oracle.com/cd/E19651-01/817-2150-10/dccorba.html
    Properties env = new Properties();
    env.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
    //env.setProperty("java.naming.provider.url", "iiop://hostname:port");

    // Se ininializa el contexto apuntando a nuestro servidor glassfish
    Context context = new InitialContext(env);

    // Se llama a algun metodo para ver que todo va bien!
    UserFacadeRemote userService = (UserFacadeRemote) context.lookup(USER_FACADE_JNDI);
    System.out.println("remote user service count response: " + userService.count());
  }
}
