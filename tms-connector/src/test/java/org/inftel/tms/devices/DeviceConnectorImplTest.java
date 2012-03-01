package org.inftel.tms.devices;

import static org.inftel.tms.domain.AlertType.DEVICE;
import static org.inftel.tms.domain.AlertType.TECHNICAL;
import static org.inftel.tms.domain.AlertType.USER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.inftel.tms.domain.Affected;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.AlertRaw;
import org.inftel.tms.domain.Device;
import org.inftel.tms.domain.Person;
import org.inftel.tms.services.AlertFacade;
import org.inftel.tms.services.AlertRawFacade;
import org.inftel.tms.services.DeviceFacade;
import org.inftel.tms.statistics.StatisticProcessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 *
 */
public class DeviceConnectorImplTest {

	static EntityManagerFactory emf;
	static EntityManager em;
	static IDatabaseConnection connection;
	static IDataSet dataset;
	static EntityTransaction tx;

	public DeviceConnectorImplTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		// Inicializar EntityManager, obtener connection y cargar datos XML
		emf = Persistence.createEntityManagerFactory("tms-persistence-mocked");
		em = emf.createEntityManager();
		connection = new DatabaseConnection(((EntityManagerImpl) (em.getDelegate()))
				.getServerSession().getAccessor().getConnection());
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		dataset = builder.build(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("deviceconector-test-dataset.xml"));
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

	@Test(expected = IllegalArgumentException.class)
	public void testProcessAlertMessageNullOrign() throws Exception {
		AlertFacade alertMocked = Mockito.mock(AlertFacade.class);
		AlertRawFacade rawMocked = Mockito.mock(AlertRawFacade.class);
		StatisticProcessor statMocked = Mockito.mock(StatisticProcessor.class);
		DeviceConnectorImpl deviceService = new DeviceConnectorImpl(alertMocked, rawMocked, null,
				statMocked);
		// No debe permitir procesar mensajes con origen nulo
		deviceService.processAlertMessage(null, "THIS_MESSAGE_MUST_NOT_BE_PARSED");
	}

	@Test
	public void testProcessAlertTypeUser() throws Exception {
		AlertFacade alertMocked = mock(AlertFacade.class);
		AlertRawFacade rawMocked = mock(AlertRawFacade.class);
		DeviceFacade devMocked = mock(DeviceFacade.class);
		StatisticProcessor statMocked = Mockito.mock(StatisticProcessor.class);
		DeviceConnectorImpl deviceService = new DeviceConnectorImpl(alertMocked, rawMocked,
				devMocked, statMocked);

		// Configuramos el metodo AlertRawFacade.create para que capture lo que le pasen
		ArgumentCaptor<AlertRaw> rawCaptor = ArgumentCaptor.forClass(AlertRaw.class);
		ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);

		// Valores esperaados
		String expectedOrigin = "617001100"; // example user alert message --v
		String expectedMessage = "*$AU11&RK123456&LD20120127&LH104737&LN1008052067&LT153052067#";

		// Configuramos deviceFacade para que devuelve un device
		when(devMocked.findByMobile(eq(expectedOrigin))).thenReturn(createDevice(expectedOrigin));

		// Se lanza el metodo
		deviceService.processAlertMessage(expectedOrigin, expectedMessage);

		// Aseguramos que se haya llamado al metodo y se captura el parameto pasado
		verify(rawMocked).create(rawCaptor.capture());
		verify(alertMocked).create(alertCaptor.capture());

		// Se asegura que el objeto que se paso esta bien definido (raw alert)
		assertEquals(expectedOrigin, rawCaptor.getValue().getOrigin());
		assertEquals(expectedMessage, rawCaptor.getValue().getRawData());

		// Se asegura que el objeto que se paso esta bien definido (alert)
		assertEquals(expectedOrigin, alertCaptor.getValue().getOrigin().getMobileNumber());
		assertEquals(USER, alertCaptor.getValue().getType());
	}

	@Test
	public void testProcessAlertTypeDevice() throws Exception {
		AlertFacade alertMocked = mock(AlertFacade.class);
		AlertRawFacade rawMocked = mock(AlertRawFacade.class);
		DeviceFacade devMocked = mock(DeviceFacade.class);
		StatisticProcessor statMocked = Mockito.mock(StatisticProcessor.class);
		DeviceConnectorImpl deviceService = new DeviceConnectorImpl(alertMocked, rawMocked,
				devMocked, statMocked);

		// Configuramos el metodo AlertRawFacade.create para que capture lo que le pasen
		ArgumentCaptor<AlertRaw> rawCaptor = ArgumentCaptor.forClass(AlertRaw.class);
		ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);

		// Valores esperaados
		String expectedOrigin = "617001100"; // example user alert message --v
		String expectedMessage = "*$AD31&RK123456&LD20120127&LH113145&LN1008052067&LT153052067&DT55#";

		// Configuramos deviceFacade para que devuelve un device
		when(devMocked.findByMobile(eq(expectedOrigin))).thenReturn(createDevice(expectedOrigin));

		// Se lanza el metodo
		deviceService.processAlertMessage(expectedOrigin, expectedMessage);

		// Aseguramos que se haya llamado al metodo y se captura el parameto pasado
		verify(rawMocked).create(rawCaptor.capture());
		verify(alertMocked).create(alertCaptor.capture());

		// Se asegura que el objeto que se paso esta bien definido (raw alert)
		assertEquals(expectedOrigin, rawCaptor.getValue().getOrigin());
		assertEquals(expectedMessage, rawCaptor.getValue().getRawData());

		// Se asegura que el objeto que se paso esta bien definido (alert)
		assertEquals(expectedOrigin, alertCaptor.getValue().getOrigin().getMobileNumber());
		assertEquals(DEVICE, alertCaptor.getValue().getType());
	}

	@Test
	public void testProcessAlertTypeTechnical() throws Exception {
		AlertFacade alertMocked = mock(AlertFacade.class);
		AlertRawFacade rawMocked = mock(AlertRawFacade.class);
		DeviceFacade devMocked = mock(DeviceFacade.class);
		StatisticProcessor statMocked = Mockito.mock(StatisticProcessor.class);
		DeviceConnectorImpl deviceService = new DeviceConnectorImpl(alertMocked, rawMocked,
				devMocked, statMocked);

		// Configuramos el metodo AlertRawFacade.create para que capture lo que le pasen
		ArgumentCaptor<AlertRaw> rawCaptor = ArgumentCaptor.forClass(AlertRaw.class);
		ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);

		// Valores esperaados
		String expectedOrigin = "617001100"; // example user alert message --v
		String expectedMessage = "*$AT2&RK123456&LD20120127&LH113147&LN1008052067&LT153052067&PB05PC000#";

		// Configuramos deviceFacade para que devuelve un device
		when(devMocked.findByMobile(eq(expectedOrigin))).thenReturn(createDevice(expectedOrigin));

		// Se lanza el metodo
		deviceService.processAlertMessage(expectedOrigin, expectedMessage);

		// Aseguramos que se haya llamado al metodo y se captura el parameto pasado
		verify(rawMocked).create(rawCaptor.capture());
		verify(alertMocked).create(alertCaptor.capture());

		// Se asegura que el objeto que se paso esta bien definido (raw alert)
		assertEquals(expectedOrigin, rawCaptor.getValue().getOrigin());
		assertEquals(expectedMessage, rawCaptor.getValue().getRawData());

		// Se asegura que el objeto que se paso esta bien definido (alert)
		assertEquals(expectedOrigin, alertCaptor.getValue().getOrigin().getMobileNumber());
		assertEquals(TECHNICAL, alertCaptor.getValue().getType());
	}

	private Device createDevice(String mobileNumber) {
		// Affected associated whit device
		Affected affected = new Affected();
		Person person = new Person();
		affected.setData(person);

		// Example deviced
		Device device = new Device();
		device.setBatery(50);
		device.setId(1l);
		device.setMobileNumber(mobileNumber);
		device.setOwner(affected);
		return device;
	}
}
