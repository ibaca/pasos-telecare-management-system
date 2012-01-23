/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import java.util.Collections;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.User;
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito;

/**
 *
 * @author ibaca
 */
public class UserFacadeTest {

  public UserFacadeTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Este test arranca una instancia de Glassfish y llama a los EJB a traves de su interfaz remoto.
   * Esto debe considerarse un test de integracion. El principal problema, instanciar el servidor y
   * desplegar los beans es muy pesado, y el test dura mas de 30s. Es poco practico como test
   * durante el desarrollo.
   *
   * En teoria podria ser mas rapido como se comenta aqui (pero no se ha conseguido)
   * http://www.adam-bien.com/roller/abien/entry/embedding_ejb_3_1_container
   * 
   * FIXME lo dejo con la etiqueta @Ignore para que no se ejectue! tarda demasiado!
   */
  @Test @Ignore
  public void testEmbeddedContainer() throws Exception {
    System.out.println("embeddable test");
    EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
    Context context = container.getContext();
    UserFacadeRemote userFacade = (UserFacadeRemote) context.lookup("java:global/classes/UserFacade");

    User entity = new User();
    entity.setName("test-user");
    entity.setPassword("secreta");
    UserFacadeRemote instance = userFacade;
    instance.create(entity);

    assertNotNull(entity);
    
    container.close();
  }

  @Test
  public void testMockedServices() throws Exception {
    // Se crear una entitymanager q usaran los ejbs
    final EntityManager testEm = Persistence.createEntityManagerFactory("tms-persistence-mocked").createEntityManager();

    // Se simulan los otros ejbs
    final DeviceFacadeRemote mockedDeviceService = Mockito.mock(DeviceFacadeRemote.class);
    // como sabemos q solo vamos a probar este metodo, solo configuramos ese metodo
    Mockito.when(mockedDeviceService.findAll()).thenReturn(Collections.<Device>emptyList());

    // Se crea un ejb y configuramos los recursos que deberia inyectar el contenedor
    UserFacade service = new UserFacade() {

      @Override
      protected EntityManager getEntityManager() {
        return testEm;
      }

      @Override
      protected DeviceFacadeRemote getDeviceFacade() {
        return mockedDeviceService;
      }
    };

    // Una vez esta todo configurado, se lanza el test
    List<Device> deviceList = service.getDevices();
    assertEquals("el usuario no debe tener dispositivos", true, deviceList.isEmpty());
  }
  
}
