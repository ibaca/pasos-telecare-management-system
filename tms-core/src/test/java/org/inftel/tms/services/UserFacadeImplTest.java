package org.inftel.tms.services;

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
import org.inftel.tms.domain.User;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ibaca
 */
public class UserFacadeImplTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public UserFacadeImplTest() {
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
            Thread.currentThread().getContextClassLoader().getResourceAsStream("user-test-dataset.xml"));
    // Todo lo realizado hasta ahora se puede hacer una unica vez para todos los test
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    em.close();
    emf.close();
  }

  @Before
  public void setUp() throws Exception {
    // La siguiente linea borra la tabla y escribe los datos del fichero XML previamente configurado
    DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    tx = em.getTransaction();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCurrentUser() throws Exception {
    // Instanciamos servicio configurado para tests
    UserFacade service = new UserFacadeImpl(em, null);

    // Se lanza el test
    tx.begin();
    User user = service.currentUser();
    tx.commit();
    assertNotNull(user);
  }
}
