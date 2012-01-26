package org.inftel.tms.domain;

import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa a los dispositivos asiciados al sistema. Cada afectado debe tener almenos un
 * dispositivo activo. Los dispositivos deben delegar las posiciones recibidas al afectado.
 * 
 * @author ibaca
 */
@Entity
@Table(name = "devices")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
        @NamedQuery(name = "Device.findById", query = "SELECT d FROM Device d WHERE d.id = :id"),
        @NamedQuery(name = "Device.findByBatery", query = "SELECT d FROM Device d WHERE d.batery = :batery"),
        @NamedQuery(name = "Device.findByCreated", query = "SELECT d FROM Device d WHERE d.created = :created"),
        @NamedQuery(name = "Device.findByLastconnection", query = "SELECT d FROM Device d WHERE d.lastConnection = :lastconnection"),
        @NamedQuery(name = Device.FIND_BY_MOBILE, query = "SELECT d FROM Device d WHERE d.mobileNumber = :mobile"),
        @NamedQuery(name = "Device.findBySimcard", query = "SELECT d FROM Device d WHERE d.simCard = :simcard"),
        @NamedQuery(name = "Device.findByTemperature", query = "SELECT d FROM Device d WHERE d.temperature = :temperature"),
        @NamedQuery(name = "Device.findByUpdated", query = "SELECT d FROM Device d WHERE d.updated = :updated"),
        @NamedQuery(name = "Device.findByVersion", query = "SELECT d FROM Device d WHERE d.version = :version") })
public class Device extends BaseEntity {

    public final static String FIND_BY_MOBILE = "Device.findByMobilenumber";
    
    private String mobileNumber;
    private String simCard;
    private Integer batery;
    private Integer temperature;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastConnection;
    @ManyToOne
    @JoinColumn(nullable = true)
    // aunq no es lo normal, puede ser nulo!
    private Affected owner;

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Affected getOwner() {
        return owner;
    }

    public void setOwner(Affected owner) {
        this.owner = owner;
    }

    public Integer getBatery() {
        return batery;
    }

    public void setBatery(Integer batery) {
        this.batery = batery;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSimCard() {
        return simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }
}
