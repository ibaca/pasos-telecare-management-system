package org.inftel.tms.statistics;

import java.util.Calendar;
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
import org.junit.*;

/**
 *
 */
public class StatisticDateUtilTest {

    static EntityManagerFactory emf;
    static EntityManager em;
    static IDatabaseConnection connection;
    static IDataSet dataset;
    static EntityTransaction tx;

    public StatisticDateUtilTest() {
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
    public void testEqualsYear() {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2012, 1, 1, 0, 0, 0);
        Date firstDay = calendar.getTime();
        calendar.set(2012, 1, 29, 0, 0, 0);
        Date lastDay = calendar.getTime();

        //Pruebas adicionales aunque no en test
        System.out.println("PRIMER DIA DEL MES: " + StatisticDateUtils.getFirstDayToMonth());
        System.out.println("ULTIMO DIA DEL MES: " + StatisticDateUtils.getLastDayToMonth());
        System.out.println("AYER: " + StatisticDateUtils.getYesterday());
        //---------------------------

        Assert.assertTrue(StatisticDateUtils.isEqualsYear(firstDay, lastDay));

    }
}
