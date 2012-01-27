package org.inftel.tms.statistics;

import static org.inftel.tms.statistics.StatisticDataPeriod.ANNUAL;
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        connection = new DatabaseConnection(((EntityManagerImpl) (em.getDelegate()))
                .getServerSession().getAccessor().getConnection());
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        dataset = builder.build(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("statistics-test-dataset.xml"));
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
    @Ignore
    public void testFindAll() throws Exception {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Assert.assertEquals(65, service.findAll().size());
    }

    @Test
    @Ignore
    public void testCount() throws Exception {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Assert.assertEquals(65, service.count());
    }

    @Test
    @Ignore
    public void testFindNyName() {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2011, 0, 1, 0, 0, 0);
        Date date = calendar.getTime();
        System.out.println("date: " + date.getTime() + "=" + date);
        for (StatisticData data : service.findAll()) {
            System.out.println(data.getName() + " > " + data.getPeriodType() + " > "
                    + data.getPeriodDate().getTime() + "=" + data.getPeriodDate() + " :: "
                    + data.getDataValue());
        }
        Map<String, Long> result = service.findStatistics("alert.type", ANNUAL, date);
        Assert.assertEquals(new Long(10), result.get("alert.type.user"));
    }

    @Test
    @Ignore
    public void testFindStatistics() {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2012, 0, 1, 0, 0, 0);
        Date firstDay = calendar.getTime();
        calendar.set(2012, 0, 20, 0, 0, 0);
        Date lastDay = calendar.getTime();
        System.out.println("First date: " + firstDay.getTime() + "=" + firstDay);
        System.out.println("Last date: " + lastDay.getTime() + "=" + lastDay);

        Map<Date, Long> result = service.findStatistics("alert.type."
                + AlertType.USER.name().toLowerCase(), DAYLY, firstDay, lastDay);
        for (Date date : result.keySet()) {
            System.out.println(date + " := " + result.get(date));
        }
        Assert.assertEquals(new Integer(18), new Integer(result.size()));
    }

    @Test
    public void testSumStatictics() {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2012, 1, 1, 0, 0, 0);
        Date firstDay = calendar.getTime();
        calendar.set(2012, 1, 29, 0, 0, 0);
        Date lastDay = calendar.getTime();
        System.out.println("First date: " + firstDay.getTime() + "=" + firstDay);
        System.out.println("Last date: " + lastDay.getTime() + "=" + lastDay);

        Map<String, BigDecimal> result = service.sumStatictics("alert.type."
                + AlertType.USER.name().toLowerCase(), DAYLY, firstDay, lastDay);

        Assert.assertEquals(60, result.get("count").longValue());

    }

    @Test
    public void testFindByDate() {
        StatisticDataFacade service = new StatisticDataFacade(em);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2012, 1, 27, 0, 0, 0);
        System.out.println("FECHA A BUSCAR: " + calendar.getTime());

        StatisticData sd = service.findByDate("alert.reciverProcessTime", calendar.getTime());

        System.out.println("id: " + sd.getId() + ">" + sd.getName() + ">" + sd.getPeriodDate()
                + ">" + sd.getPeriodType() + ">" + sd.getDataCount() + ">" + sd.getDataValue()
                + ">" + sd.getDataSum());

        Assert.assertEquals(new Double(200), sd.getDataSum());
        // Assert.assertNull(sd);

    }
}
