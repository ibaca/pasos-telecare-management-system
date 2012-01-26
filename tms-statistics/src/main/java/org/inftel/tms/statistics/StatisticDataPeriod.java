package org.inftel.tms.statistics;

/**
 * Periodo del dato estadistico. Estable el intervalo de tiempo que representa cada dato
 * estadistico, pro ejemplo, si el periodo fuese emnsual significa que los valores estadisticos
 * almacenados corresponderían al total acumulado de ese mes.
 * 
 * @author ibaca
 */
public enum StatisticDataPeriod {
    /**
     * Periodo estadistico para intervalo diario.
     */
    DAYLY,
    /**
     * Periodo estadistico para intervalo mensual.
     */
    MONTHLY,
    /**
     * Periodo estadistico para intervalo anual.
     */
    ANNUAL
}