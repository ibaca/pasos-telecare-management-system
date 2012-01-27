package org.inftel.tms.statistics;

import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
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
public class StatisticData implements Serializable {

    public static final String LAST_MONTH = "StatisticsData.LastMonth";

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "statistic_data_generator", initialValue = 10000)
    @GeneratedValue(strategy = TABLE, generator = "statistic_data_generator")
    private Long id;
    private String name;
    @Column(nullable = false)
    private StatisticDataPeriod periodType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodDate;
    private Double dataSum;
    private Long dataCount;

    public StatisticData() {
    }

    public StatisticData(String name, StatisticDataPeriod period, Date date, Long count) {
        this(name, period, date, null, count);
    }

    public StatisticData(String name, StatisticDataPeriod period, Date date, Double sum) {
        this(name, period, date, sum, null);
    }
    
    /** Este metodo crea estadisticas temporales, ya que el periodo es un campo obligatorio. */
    public StatisticData(String name, Date date, Double sum, Long count) {
        this(name, null, date, sum, count);
    }

    public StatisticData(String name, StatisticDataPeriod period, Date date, Double sum, Long count) {
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
        return (dataCount == null) ? 1l : dataCount;
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
        return dataSum;
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
        if (!(object instanceof StatisticData)) {
            return false;
        }
        StatisticData other = (StatisticData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.tms.statistics.StatisticsData[ id=" + id + " ]";
    }
}
