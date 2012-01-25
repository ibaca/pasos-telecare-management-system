package org.inftel.tms;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.*;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;

/**
 * Inicializa valores al arrancar la aplicacion.
 *
 * Actualmente se usa únicamente para inicializar algunos datos en la base de datos para facilitar
 * el desarrollo.
 *
 * @author ibaca
 */
@Startup
@Singleton
@LocalBean
public class StartupApplication {

  private static final Logger log = Logger.getLogger(StartupApplication.class.getName());
  @PersistenceContext(unitName = "tms-persistence")
  private EntityManager em;

  public StartupApplication() {
  }

  @PostConstruct
  private void populateData() {
    log.info("agregando datos iniciales en la base de datos");
    try {
      Connection wrap = em.unwrap(Connection.class);
      // FIXME mala solucion, pero obtener la conexion difiere de test a desplegado
      if (wrap == null) {
        wrap = ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection();
      }
      IDatabaseConnection connection = new DatabaseConnection(wrap);
      FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
      IDataSet dataset = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("sample-dataset.xml"));
      DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    } catch (Exception e) {
      log.log(Level.WARNING, "fallo mientras se intentaba añadir datos iniciales al modelo", e);
    }
  }

  /**
   * Constructor interno usado en los test.
   *
   * @param em gestor de entidades
   */
  StartupApplication(EntityManager em) {
    this.em = em;
    populateData(); // Imitate PostConstruct
  }
}
