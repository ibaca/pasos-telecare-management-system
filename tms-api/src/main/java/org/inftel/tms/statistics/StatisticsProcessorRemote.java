package org.inftel.tms.statistics;

import java.util.Date;

import javax.ejb.Remote;

/**
 * Permite registrar valores estadisticos de forma asincrona.
 * 
 * @author ibaca
 */
@Remote
public interface StatisticsProcessorRemote {

    /**
     * Envia a procesar una estadistica. Por ejemplo, si se quiere almacenar el tiempo de procesado
     * de los mensajes recibidos para 10 minsajes alavez:
     * <ul>
     * <li>name: 'system.messagesRecived.processTime'
     * <li>date: new Date()
     * <li>accumulated: 300 (si la muestra duro 300milisegundos en procesarse)
     * </ul>
     * 
     * Este metodo registra una única muestra, si se quiere registrar varias muestras simultaneas
     * debe llamarse al metodo
     * {@link StatisticsProcessor#queueStatistic(String, Calendar, int, double)}.
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param accumulated
     *            el valor acumulado que representa la muestra
     */
    public void process(String name, Date date, Double accumulated, Long samples);

    /**
     * Envia a procesar una estadistica tipo acumulado. Por ejemplo, si se quiere almacenar el
     * tiempo de procesado de los mensajes recibidos:
     * <ul>
     * <li>name: 'system.messagesRecived.processTime'
     * <li>date: new Date()
     * <li>accumulated: 300 (si la muestra duro 300milisegundos en procesarse)
     * </ul>
     * 
     * Este metodo registra una única muestra, si se quiere registrar varias muestras simultaneas
     * debe llamarse al metodo
     * {@link StatisticsProcessor#queueStatistic(String, Calendar, int, double)}.
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param accumulated
     *            el valor acumulado que representa la muestra
     */
    public void process(String name, Date date, double accumulated);

    /**
     * Envia a procesar una estadistica tipo contador. Por ejemplo, si se quiere almacenar el numero
     * de mensajes los valores podrian ser:
     * <ul>
     * <li>name: 'system.messagesRecived.count'
     * <li>date: new Date()
     * <li>samples: 5 (si se quieren registrar 5 mensajes recibidos en la fecha <code>date</code>)
     * </ul>
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     * @param samples
     *            numero de muestras que contienen se quieren registrar
     */
    public void process(String name, Date date, long samples);

    /**
     * Envia a procesar una estadistica tipo contador. Por ejemplo, si se quiere almacenar el numero
     * de mensajes los valores podrian ser:
     * <ul>
     * <li>name: 'system.messagesRecived.count'
     * <li>date: new Date()
     * <li>samples: 5 (si se quieren registrar 5 mensajes recibidos en la fecha <code>date</code>)
     * </ul>
     * 
     * @param name
     *            del valor estadistico en nomenclatura similar a paquetes java
     * @param date
     *            fecha asociada al valor estadistico
     */
    public void process(String name, Date date);
}
