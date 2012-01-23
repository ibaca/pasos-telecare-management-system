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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
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

  /**
   * Este test muestra como llamar y ejecutar los EJB sin necesidad de arrancar un contenedor EJB.
   * Para esto deben configurarse y simularse todas las dependencias que cada EJB pueda tener.
   */
  @Test
  public void testMockedServices() throws Exception {
    // Se crear una entitymanager q usaran los ejbs
    final EntityManager em = Persistence.createEntityManagerFactory("tms-persistence-mocked").createEntityManager();

    // Se simulan los otros ejbs
    final DeviceFacadeRemote mockedDeviceService = Mockito.mock(DeviceFacadeRemote.class);
    // como sabemos q solo vamos a probar este metodo, solo configuramos ese metodo
    Mockito.when(mockedDeviceService.findAll()).thenReturn(Collections.<Device>emptyList());

    // Se crea un ejb y configuramos los recursos que deberia inyectar el contenedor
    UserFacade service = new UserFacade(em, mockedDeviceService);

    // Una vez esta todo configurado, se lanza el test
    List<Device> deviceList = service.getDevices();
    assertEquals("el usuario no debe tener dispositivos", true, deviceList.isEmpty());

    em.close();
  }

  /**
   * Este test muestra como rellenar una tabla con datos desde un XML, de forma que cada test se
   * pueda realizar sobre los mismos datos.
   *
   * Basado en el ejemplo http://www.antoniogoncalves.org/xwiki/bin/view/Article/TestingJPA
   */
  @Test
  public void testFindAll() throws Exception {
    // Inicializar EntityManager, obtener connection y cargar datos XML
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("tms-persistence-mocked");
    EntityManager em = emf.createEntityManager();
    IDatabaseConnection connection = new DatabaseConnection(
            ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection());
    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
    IDataSet dataset = builder.build(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("user-test-dataset.xml"));
    // Todo lo realizado hasta ahora se puede hacer una unica vez para todos los test
    
    EntityTransaction tx = em.getTransaction();

    // La siguiente linea borra la tabla y escribe los datos del fichero XML previamente configurado
    DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    
    // Instanciamos servicio configurado para tests
    UserFacadeRemote service = new UserFacade(em,null);
    
    // Se lanza el test
    tx.begin();
    List<User> userList = service.findAll();
    tx.commit();
    assertEquals(5, userList.size());
    
    // Imprimo solo por ver como ha mapeado los roles
    for(User user: userList) {
      System.out.println(user + " as " + user.getUserRole());
    }
            
    // Se liberan los recursos
    em.close();
    emf.close();
  }
}
