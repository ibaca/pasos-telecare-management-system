/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.services;

import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import org.inftel.tms.domain.User;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ibaca
 */
public class UserFacadeTest {

  private static EJBContainer container;
  private static Context context;
  private static UserFacadeRemote userFacade;

  public UserFacadeTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
    context = container.getContext();
    userFacade = (UserFacadeRemote) context.lookup("java:global/classes/UserFacade");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    container.close();
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of create method, of class UserFacade.
   */
  @Test
  public void testCreate() throws Exception {
    System.out.println("create");
    User entity = new User();
    entity.setName("test-user");
    entity.setPassword("secreta");
    UserFacadeRemote instance = userFacade;
    instance.create(entity);

    assertNotNull(entity);
  }

  /**
   * Test of edit method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testEdit() throws Exception {
    System.out.println("edit");
    User entity = null;
    UserFacadeRemote instance = userFacade;
    instance.edit(entity);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of remove method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testRemove() throws Exception {
    System.out.println("remove");
    User entity = null;
    UserFacadeRemote instance = userFacade;
    instance.remove(entity);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of find method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testFind() throws Exception {
    System.out.println("find");
    Object id = null;
    UserFacadeRemote instance = userFacade;
    User expResult = null;
    User result = instance.find(id);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of findAll method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testFindAll() throws Exception {
    System.out.println("findAll");
    UserFacadeRemote instance = userFacade;
    List expResult = null;
    List result = instance.findAll();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of findRange method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testFindRange() throws Exception {
    System.out.println("findRange");
    int[] range = null;
    UserFacadeRemote instance = userFacade;
    List expResult = null;
    List result = instance.findRange(range);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of count method, of class UserFacade.
   */
  @Test
  @Ignore
  public void testCount() throws Exception {
    System.out.println("count");
    UserFacadeRemote instance = userFacade;
    int expResult = 0;
    int result = instance.count();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
