package org.inftel.tms.statistics;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.domain.Intervention;

/**
 * Debe sacar la informacion necesaria a traves de los metodos expuestos en el
 * interfaz remoto, y poner en la cola de estadisticas los valores concretos que
 * se quieran agregar.
 *
 * La decision de extraer las estadisticas de la entidad concreta (alert,
 * interventio, etc) podria hacerse cuando se lee de la cola.
 */
@Stateless
public class StatisticsProcessor implements StatisticsProcessorRemote {

    @Resource(mappedName = "jms/statistics")
    private Queue statistics;
    @Resource(mappedName = "jms/statisticsFactory")
    private ConnectionFactory statisticsFactory;

    @Override
    public void processAlert(Alert name) {
        // Ejemplos de estadisticas
        // alert.recived : numero de alertas recibidas
        //
        // alert.type.user : numero de alertas por tipo
        // alert.type.device
        // alert.type.technical
        // ...
        // alert.priority.critical : numero de alertas por prioridad
        // alert.priority.important
        // ...
    }

    @Override
    public void processIntervention(Intervention intervention) {
    }

    private Message createJMSMessageForjmsStatistics(Session session, Serializable messageData) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage tm = session.createObjectMessage(messageData);
        return tm;
    }

    private void sendJMSMessageToStatistics(Serializable messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = statisticsFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(statistics);
            messageProducer.send(createJMSMessageForjmsStatistics(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
