package org.inftel.tms.statistics;

import java.util.Calendar;

import javax.ejb.Local;

@Local
public interface StatisticsProcessorLocal {
    public void updateStatistic(String statisticName, Calendar date, int value); 
}
