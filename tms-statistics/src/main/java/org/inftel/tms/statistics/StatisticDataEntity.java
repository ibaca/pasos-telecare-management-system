package org.inftel.tms.statistics;

import static java.util.logging.Level.INFO;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Los valores estadisticos quedan definidos por dos atributos, sum y count. Sum indica el sumatorio
 * de los valores en el intervalo que representa la instancia. Count representa el numero de
 * muestras usadas en el intervalo.
 * 
 * Existen valores que solo usaran uno de los atributos, por ejemplo, si se guarda una estadistica
 * de mensajes procesados se registrara únicamente el count, que represnta el numero de menjsajes,
 * dejando sum igual a null.
 * 
 * Se ha creado un metodo de conveniencia getDataValue, que permite obtener el valor estadistico
 * independientemente de si se trata de un contador o un sumatorio.
 * 
 * @author agumpg
 */
@Entity
@Table(name = "statistics")
public class StatisticDataEntity implements Serializable, StatisticData {

	private static final Logger logger = Logger.getLogger(StatisticDataEntity.class.getName());

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String name;

	@Column(nullable = false)
	private StatisticDataPeriod periodType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date periodDate;

	private Double dataSum;

	private Long dataCount;

	public StatisticDataEntity() {
	}

	public StatisticDataEntity(String name, StatisticDataPeriod period, Date date, Long count) {
		this(name, period, date, null, count);
	}

	public StatisticDataEntity(String name, StatisticDataPeriod period, Date date, Double sum) {
		this(name, period, date, sum, null);
	}

	/** Este metodo crea estadisticas temporales, ya que el periodo es un campo obligatorio. */
	public StatisticDataEntity(String name, Date date, Double sum, Long count) {
		this(name, null, date, sum, count);
	}

	public StatisticDataEntity(String name, StatisticDataPeriod period, Date date, Double sum, Long count) {
		this.name = name;
		this.periodType = period;
		this.periodDate = date;
		this.dataSum = sum;
		this.dataCount = count; // Salta la restriccion de valores negativos o nulos
	}

	public BigDecimal getDataValue() {
		if (dataSum == null) {
			// Si sum es null se devuelve el valor del contador
			return new BigDecimal(getDataCount());
		} else if (dataCount == null) {
			return new BigDecimal(getDataSum());
		} else {
			// Si no, se devolvera el valor de sum divido entre el contador
			return new BigDecimal(getDataSum()).divide(new BigDecimal(getDataCount()));
		}
	}

	public Long getDataCount() {
		return (dataCount == null) ? 0l : dataCount;
	}

	public void setDataCount(Long dataCount) {
		if (dataCount == null || dataCount < 0) {
			// Si dataCount == 0 podria dar una excepcion al llamar a getDataValue (division por 0)
			throw new IllegalArgumentException("el contador debe ser valor entero mayor que 0");
		}
		this.dataCount = dataCount;
	}

	public Date getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(Date periodDate) {
		this.periodDate = periodDate;
	}

	public StatisticDataPeriod getPeriodType() {
		return periodType;
	}

	public void setPeriodType(StatisticDataPeriod periodType) {
		this.periodType = periodType;
	}

	public Double getDataSum() {
		return (dataSum == null) ? 0d : dataSum;
	}

	public void setDataSum(Double dataSum) {
		this.dataSum = dataSum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof StatisticDataEntity)) {
			return false;
		}
		StatisticDataEntity other = (StatisticDataEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("StatisticsData[");
		buff.append(" id=").append(id);
		buff.append(", name=").append(name);
		buff.append(", period=").append(periodType);
		buff.append(", date=").append(periodDate);
		buff.append(", count=").append(dataCount);
		buff.append(", sum=").append(dataSum);
		buff.append(" ]");
		return buff.toString();
	}

	@PostPersist
	void onCreateLog() {
		logger.log(INFO, "post persist {0}", toString());
	}

	@PreUpdate
	void onUpdate() {
		logger.log(INFO, "pre update {0}", toString());
	}
}
