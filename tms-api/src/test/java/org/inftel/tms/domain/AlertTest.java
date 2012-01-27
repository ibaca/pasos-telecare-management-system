/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ibaca
 */
public class AlertTest {

  private static EntityManagerFactory emf;
  private static EntityManager em;
  private static EntityTransaction tx;

  public AlertTest() {
  }

  @BeforeClass
  public static void initEntityManager() throws Exception {
    emf = Persistence.createEntityManagerFactory("tms-persistence");
    em = emf.createEntityManager();
  }

  @AfterClass
  public static void closeEntityManager() {
    em.close();
    emf.close();
  }

  @Before
  public void setUp() {
    tx = em.getTransaction();
  }

  @After
  public void tearDown() {
  }

  @Test(expected = RollbackException.class)
  public void testAlertNeedRaw() {
    System.out.println("alertNeedRaw");
    Alert instance = new Alert();

    tx.begin();
    em.persist(instance);
    tx.commit();
    fail("No se puede guardar una alerta sin una AlertRaw asociado");
  }
  
  @Test
  public void testAlertCreate() {
    System.out.println("alertNeedRaw");
    
    AlertRaw needed = new AlertRaw();
    needed.setOrigin("666123123");
    needed.setRawData("#EZ?1234$1234%");

    Alert instance = new Alert();
    instance.setRaw(needed);
    
    tx.begin();
    Person person = new Person();
    person.setEmail("mail@mail.com");
    Affected affected = new Affected();
    affected.setData(person);
    em.persist(person);
    em.persist(affected);
    tx.commit();
    
    tx.begin();
    Device device = new Device();
    device.setOwner(affected);
    em.persist(device);
    tx.commit();
    
    instance.setAffected(device.getOwner().getData());
    
    tx.begin();
    em.persist(needed);
    em.persist(instance);
    tx.commit();
    
    Long alertRawId = needed.getId();
    AlertRaw raw = em.find(AlertRaw.class, alertRawId);
    Alert alert = raw.getAlert();
    assertNotNull(alert);
  }
}
