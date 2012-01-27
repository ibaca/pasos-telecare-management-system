package org.inftel.tms.statistics;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.inftel.tms.statistics.StatisticDataPeriod.ANNUAL;
import static org.inftel.tms.statistics.StatisticDataPeriod.DAYLY;
import static org.inftel.tms.statistics.StatisticDataPeriod.MONTHLY;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * Debe sacar la informacion necesaria a traves de los metodos expuestos en el interfaz remoto, y
 * poner en la cola de estadisticas los valores concretos que se quieran agregar.
 * 
 * La decision de extraer las estadisticas de la entidad concreta (alert, interventio, etc) podria
 * hacerse cuando se lee de la cola.
 */
@Stateless
public class StatisticsProcessor implements StatisticsProcessorRemote, StatisticsProcessorLocal {

    private final static Logger logger = getLogger(StatisticsProcessor.class.getName());

    @EJB
    private StatisticDataFacade statisticDataFacade;
    @Resource(mappedName = "jms/statistics")
    private Queue statistics;
    @Resource(mappedName = "jms/statisticsFactory")
    private ConnectionFactory statisticsFactory;

    @Override
    public void process(String name, Date date) {
        process(name, date, 1);
    }

    @Override
    public void process(String name, Date date, long samples) {
        process(name, date, null, samples);
    }

    @Override
    public void process(String name, Date date, double accumulated) {
        process(name, date, accumulated, null);
    }

    @Override
    public void process(String name, Date date, Double accumulated, Long samples) {
        try {
            // Crea una StatisticData temporal, es decir, sin periodo definido
            sendJMSMessageToStatistics(new StatisticData(name, date, accumulated, samples));
        } catch (JMSException ex) {
            logger.log(WARNING, "fallo enviando estadistica a la cola de proceso", ex);
        }
    }

    private Message createJMSMessageForjmsStatistics(Session session, Serializable messageData)
            throws JMSException {
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
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                            "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void updateStatistic(String statisticName, Calendar date, int value) {
        // Se calcula el dia de hoy para comparar
        Calendar today = Calendar.getInstance();

        // Actualización de diarios de Alertas
        saveStatisticData(statisticName, DAYLY, date, value);

        // Si ayer fue un mes diferente
        if (today.get(MONTH) != date.get(MONTH)) {
            int count = statisticDataFacade.sumStatictics(statisticName, DAYLY, MONTHLY
                    .endsAt(date).getTime(), MONTHLY.beginsAt(date).getTime());
            saveStatisticData(statisticName, MONTHLY, date, (long) count);
        }

        // Si se ha producido un cambio de año actualizamos históricos anuales
        if (today.get(YEAR) != date.get(YEAR)) {
            int count = statisticDataFacade.sumStatictics(statisticName, MONTHLY,
                    ANNUAL.endsAt(date).getTime(), ANNUAL.beginsAt(date).getTime());
            saveStatisticData(statisticName, ANNUAL, date, (long) count);
        }
    }

    private void saveStatisticData(String name, StatisticDataPeriod period, Calendar date,
            long value) {
        StatisticData sd = new StatisticData();

        sd.setName(name);
        sd.setPeriodType(period);
        sd.setPeriodDate(period.beginsAt(date).getTime());
        sd.setDataCount(value);

        statisticDataFacade.create(sd);

    }

    @Override
    public Map<Date, Long> findStatistics(String name, StatisticDataPeriod period, Date fromDate,
            Date toDate) {
        logger.log(INFO, "consultando estadisticas {0} para periodo {1} y fechas entre {2} y {3}",
                new Object[] { name, period, fromDate, toDate });
        // Delegate to StatisticDataFacade
        return statisticDataFacade.findStatistics(name, period, fromDate, toDate);
    }

    @Override
    public List<String> findStatisticsNames(String startWith) {
        return statisticDataFacade.findStatisticsNames(startWith);
    }

}
