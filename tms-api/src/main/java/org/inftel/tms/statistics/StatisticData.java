package org.inftel.tms.statistics;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ibaca
 */
public interface StatisticData {

	Long getDataCount();

	Double getDataSum();

	BigDecimal getDataValue();

	String getName();

	Date getPeriodDate();

	StatisticDataPeriod getPeriodType();
	
}
