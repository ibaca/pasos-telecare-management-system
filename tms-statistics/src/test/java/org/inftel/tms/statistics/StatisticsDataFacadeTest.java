package org.inftel.tms.statistics;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.commons.lang.time.DateUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.junit.*;

/**
 *
 */
public class StatisticsDataFacadeTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public StatisticsDataFacadeTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    // Inicializar EntityManager, obtener connection y cargar datos XML
    emf = Persistence.createEntityManagerFactory("tms-statistic-mocked");
    em = emf.createEntityManager();
    connection = new DatabaseConnection(
            ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection());
    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
    dataset = builder.build(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("statistics-test-dataset.xml"));
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
  public void testFindAll() throws Exception {
    StatisticsDataFacade service = new StatisticsDataFacade(em);
    Assert.assertEquals(6, service.findAll().size());
  }

  @Test
  public void testCount() throws Exception {
    StatisticsDataFacade service = new StatisticsDataFacade(em);
    Assert.assertEquals(6, service.count());
  }

  @Test
  public void testFindNyName() {
    StatisticsDataFacade service = new StatisticsDataFacade(em);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);
    calendar.set(2011, 0, 1, 0, 0, 0);
    Date date = calendar.getTime();
    System.out.println("date: " + date.getTime() + "=" + date);
    for (StatisticsData data : service.findAll()) {
      System.out.println(data.getName() + " > " + data.getDataPeriod() + " > " + data.getDataDate().getTime() + "="
              + data.getDataDate() + " :: " + data.getDataValue());
    }
    Map<String, Long> result = service.findStatistics("alert.type", StatisticsData.statisticPeriod.ANNUAL, date);
    Assert.assertEquals(new Long(10), result.get("alert.type.user"));
  }
  
  
}
