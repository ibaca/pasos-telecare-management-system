
package org.inftel.tms.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representan las alertas recibidas de los dispositivos. Que info se guarda? Lo
 * interesante es guardar un par de indices tipo/subtipo que permita hacer
 * busquedas rapidas. Ademas alguna columna booleana que ponga el estado
 * abierto/cerrado, esto independientemente de si se guarda un historico de
 * acciones o intevenciones.
 * 
 * @author ibaca
 */
@Entity
@Table(name = "alerts")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a"),
        @NamedQuery(name = "Alert.findById", query = "SELECT a FROM Alert a WHERE a.id = :id"),
        @NamedQuery(name = "Alert.findByCause", query = "SELECT a FROM Alert a WHERE a.cause = :cause"),
        @NamedQuery(name = "Alert.findByCreated", query = "SELECT a FROM Alert a WHERE a.created = :created"),
        @NamedQuery(name = "Alert.findByPriority", query = "SELECT a FROM Alert a WHERE a.priority = :priority"),
        @NamedQuery(name = "Alert.findByType", query = "SELECT a FROM Alert a WHERE a.type = :type"),
        @NamedQuery(name = "Alert.findByUpdated", query = "SELECT a FROM Alert a WHERE a.updated = :updated"),
        @NamedQuery(name = "Alert.findByVersion", query = "SELECT a FROM Alert a WHERE a.version = :version"),
        @NamedQuery(name = Alert.FIND_BY_AFFECTED, query = "SELECT a FROM Alert a WHERE a.affected = :affected"),
        @NamedQuery(name = Alert.FIND_ACTIVED, query = "SELECT a FROM Alert a WHERE a.closedIntervention IS NULL"),
        @NamedQuery(name = Alert.COUNT_BY_TYPE, query = "SELECT count(a) FROM Alert a WHERE a.type = :type AND a.created BETWEEN :fromDate AND :toDate")
})
public class Alert extends BaseEntity {

    private static final long serialVersionUID = 3785001990323071857L;
    public static final String FIND_ACTIVED = "Alert.findActived";
    public static final String FIND_BY_AFFECTED = "Alert.findByAffected";
    public static final String COUNT_BY_TYPE = "Alert.countByType";
    private AlertType type;
    private AlertPriority priority;
    private String cause;
    private Double latitude;
    private Double longitude;
    @OneToOne
    private Device origin;
    @ManyToOne
    private Person affected;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(unique = true, nullable = false, updatable = false)
    private AlertRaw raw;
    @OneToMany(mappedBy = "alert")
    private List<Intervention> interventions;
    @OneToOne(optional = true)
    private Intervention closedIntervention;

    /**
     * Intervencion que cerro la alerta. Si en caso de ser nulo la alerta se
     * considera activa.
     * 
     * @return intervencion que cerro la alerta
     */
    public Intervention getClosedIntervention() {
        return closedIntervention;
    }

    /**
     * Establece la intervencion que cierra la alerta. Si se pasa
     * <code>null</code> se cambia la alerta a estado activo. La intervencion
     * pasada debe pertenecer a la lista devuelta por
     * {@link #getInterventions()}.
     * 
     * @param closedIntervention intervencion que cierra la alerta
     */
    public void setClosedIntervention(Intervention closedIntervention) {
        this.closedIntervention = closedIntervention;
    }

    /**
     * Lista de intervenciones asociadas a la alerta
     * 
     * @return lista de las intervenciones
     */
    public List<Intervention> getInterventions() {
        return interventions;
    }

    // FIXME esto no estoy seguro de q sea seguro! ;)
    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }

    /**
     * Persona afectada por la alerta.
     * 
     * @return persona afectada por la alerta
     */
    public Person getAffected() {
        return affected;
    }

    /**
     * Establece la persona afectada por la alerta.
     * 
     * @param affected la persona afectada por la alerta
     */
    public void setAffected(Person affected) {
        this.affected = affected;
    }

    /**
     * Mensaje descriptivo que indica el motivo de la alerta.
     * 
     * @return el mensaje que descrive el motivo
     */
    public String getCause() {
        return cause;
    }

    /**
     * Establece el motivo de la alerta.
     * 
     * @param cause el motivo de la alerta
     */
    public void setCause(String cause) {
        this.cause = cause;
    }

    /**
     * Dispositivo que origino la alerta.
     * 
     * @return el dispositivo origen de la alerta
     */
    public Device getOrigin() {
        return origin;
    }

    /**
     * Establece el dispositivo que origino la alerta. Este dispositivo debe
     * deducirse a partir de la informacion obtenida de
     * {@link AlertRaw#getOrigin()}.
     * 
     * @param origin dispositivo origen de la alerta
     */
    public void setOrigin(Device origin) {
        this.origin = origin;
    }

    /**
     * Obtiene la prioridad de la alerta.
     * 
     * @return la prioridad de la alerta
     */
    public AlertPriority getPriority() {
        return priority;
    }

    /**
     * Establece la prioridad de la alerta.
     * 
     * @param priority prioridad de la alerta
     */
    public void setPriority(AlertPriority priority) {
        this.priority = priority;
    }

    /**
     * Mensaje original de la alerta sin parsear.
     * 
     * @return mensaje original sin parsear
     */
    public AlertRaw getRaw() {
        return raw;
    }

    /**
     * Establece el mensjae original sin parsear. Toda alerta debe tenr un
     * mensaje origen sin parsear.
     * 
     * @param raw el mensaje original sin parsear
     */
    public void setRaw(AlertRaw raw) {
        this.raw = raw;
        raw.setAlert(this);
    }

    /**
     * Obtiene el tipo de alerta.
     * 
     * @return tipo de alerta
     */
    public AlertType getType() {
        return type;
    }

    /**
     * Establece el tipo de alerta. Toda alerta debe tener un tipo.
     * 
     * @param type el tipo de alerta
     */
    public void setType(AlertType type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
