package org.inftel.tms.devices;

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
import org.inftel.tms.domain.AlertRaw;
import org.inftel.tms.services.AlertFacadeRemote;
import org.inftel.tms.services.AlertRawFacadeRemote;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 *
 */
public class DeviceConnectorTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public DeviceConnectorTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    // Inicializar EntityManager, obtener connection y cargar datos XML
    emf = Persistence.createEntityManagerFactory("tms-persistence-mocked");
    em = emf.createEntityManager();
    connection = new DatabaseConnection(
            ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection());
    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
    dataset = builder.build(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("deviceconector-test-dataset.xml"));
    // Todo lo realizado hasta ahora se puede hacer una unica vez para todos los test
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    em.close();
    emf.close();
  }

  @Before
  public void setUp() throws Exception {
    DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    tx = em.getTransaction();
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of processAlertMessage method, of class DeviceConnector.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testProcessAlertMessageNullOrign() throws Exception {
    AlertFacadeRemote alertMocked = Mockito.mock(AlertFacadeRemote.class);
    AlertRawFacadeRemote rawMocked = Mockito.mock(AlertRawFacadeRemote.class);
    DeviceConnectorRemote deviceService = new DeviceConnector(alertMocked, rawMocked);
    // No debe permitir procesar mensajes con origen nulo
    deviceService.processAlertMessage(null, "MESSAGE");
  }

  @Test
  public void testProcessAlertMessageRawSave() throws Exception {
    AlertFacadeRemote alertMocked = Mockito.mock(AlertFacadeRemote.class);
    AlertRawFacadeRemote rawMocked = Mockito.mock(AlertRawFacadeRemote.class);
    DeviceConnectorRemote deviceService = new DeviceConnector(alertMocked, rawMocked);

    // Configuramos el metodo AlertRawFacade.create para que capture lo que le pasen
    ArgumentCaptor<AlertRaw> rawCaptor = ArgumentCaptor.forClass(AlertRaw.class);
    
    // Se lanza el metodo
    String expectedOrigin = "617001100";
    String expectedMessage = "MESSAGE";
    deviceService.processAlertMessage(expectedOrigin, expectedMessage);
    
    // Aseguramos que se haya llamado al metodo y se captura el parameto pasado
    Mockito.verify(rawMocked).create(rawCaptor.capture());
    
    // Se asegura que el objeto que se paso esta bien definido
    Assert.assertEquals(expectedOrigin, rawCaptor.getValue().getOrigin());
    Assert.assertEquals(expectedMessage, rawCaptor.getValue().getRawData());
  }
}
