/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ibaca
 */
public class UserTest {

  private static EntityManagerFactory emf;
  private static EntityManager em;
  private static EntityTransaction tx;

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
  public void initTransaction() {
    tx = em.getTransaction();
  }

  @Test
  public void createArtist() throws Exception {

    User user = new User();
    user.setName("ambrosio");
    user.setPassword("secreta");
    tx.begin();
    em.persist(user);
    tx.commit();
    Assert.assertNotNull("ID should not be null", user.getId());
  }
}
