package org.inftel.tms.services;

import java.util.Date;
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
import org.inftel.tms.domain.AlertType;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ibaca
 */
public class AlertFacadeTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public AlertFacadeTest() {
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
            Thread.currentThread().getContextClassLoader().getResourceAsStream("alert-test-dataset.xml"));
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

  @Test
  public void testCount() throws Exception {
    AlertFacadeRemote service = new AlertFacade(em);
    int count = service.count();
    assertEquals(5, count);
  }
  
  @Test
  public void testCountByType() throws Exception {
    AlertFacadeRemote service = new AlertFacade(em);
    tx.begin();
    int count = service.countByType(AlertType.USER, new Date(0), new Date());
    tx.commit();
    assertEquals(3, count);
    
    tx.begin();
    count = service.countByType(AlertType.DEVICE, new Date(0), new Date());
    tx.commit();
    assertEquals(2, count);
    
    tx.begin();
    count = service.countByType(AlertType.TECHNICAL, new Date(0), new Date());
    tx.commit();
    assertEquals(0, count);
  }
}
