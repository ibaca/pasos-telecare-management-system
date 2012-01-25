package org.inftel.tms;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ibaca
 */
public class StartupApplicationTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public StartupApplicationTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    emf = Persistence.createEntityManagerFactory("tms-persistence-mocked");
    em = emf.createEntityManager();
    connection = new DatabaseConnection(((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection());
    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
    dataset = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("sample-dataset.xml"));
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    em.close();
    emf.close();
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCount() throws Exception {
    @SuppressWarnings("unused")
    StartupApplication service = new StartupApplication(em); // Puebla la tabla
    ITable table = connection.createQueryTable("alerts", "select * from alerts");
    assertEquals(5, table.getRowCount());
  }
}
