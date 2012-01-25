package org.inftel.tms.statistics;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author agumpg
 */
@Entity
@Table(name = "statistic_data")
public class StatisticsData implements Serializable {

    public static enum statisticPeriod {

        DAYLY, MONTHLY, ANNUAL
    };
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private statisticPeriod dataPeriod;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dataDate;
    private Long dataValue;

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public statisticPeriod getDataPeriod() {
        return dataPeriod;
    }

    public void setDataPeriod(statisticPeriod dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    public Long getDataValue() {
        return dataValue;
    }

    public void setDataValue(Long dataValue) {
        this.dataValue = dataValue;
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
        if (!(object instanceof StatisticsData)) {
            return false;
        }
        StatisticsData other = (StatisticsData) object;
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
