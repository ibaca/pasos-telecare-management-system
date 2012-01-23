package org.inftel.tms.domain;

import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representan las alertas recibidas de los dispositivos.
 *
 * Que info se guarda? Lo interesante es guardar un par de indices tipo/subtipo que permita hacer
 * busquedas rapidas. Ademas alguna columna booleana que ponga el estado abierto/cerrado, esto
 * independientemente de si se guarda un historico de acciones o intevenciones.
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
  @NamedQuery(name = Alert.FIND_ACTIVED, query = "SELECT a FROM Alert a WHERE a.closedIntervention IS NULL")})
public class Alert extends BaseEntity {
  
  public static final String FIND_ACTIVED = "Alert.findActived";
  public static final String FIND_BY_AFFECTED = "Alert.findByAffected";

  // FIXME no puede cambiarse el orden del enumerado
  // http://duydo.com/effective-jpa-persist-an-enumerationeffectively/
  public static enum AlarmType {

    USER, DEVICE, TECHNICAL
  };

  public static enum AlarmPriority {

    CRITICAL, IMPORTANT, HIGH, NORMAL, LOW, INFO;
  }
  private AlarmType type;
  private AlarmPriority priority;
  private String cause;
  @OneToOne
  private Device origin;
  @ManyToOne
  private Person affected;
  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(unique = true, nullable = false, updatable = false)
  private AlertRaw raw;
  @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL)
  private List<Intervention> interventions;
  @OneToOne(optional = true)
  private Intervention closedIntervention;

  public Intervention getClosedIntervention() {
    return closedIntervention;
  }

  public void setClosedIntervention(Intervention closedIntervention) {
    this.closedIntervention = closedIntervention;
  }

  public List<Intervention> getInterventions() {
    return interventions;
  }

  public void setInterventions(List<Intervention> interventions) {
    this.interventions = interventions;
  }

  public Person getAffected() {
    return affected;
  }

  public void setAffected(Person affected) {
    this.affected = affected;
  }

  public String getCause() {
    return cause;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }

  public Device getOrigin() {
    return origin;
  }

  public void setOrigin(Device origin) {
    this.origin = origin;
  }

  public AlarmPriority getPriority() {
    return priority;
  }

  public void setPriority(AlarmPriority priority) {
    this.priority = priority;
  }

  public AlertRaw getRaw() {
    return raw;
  }

  public void setRaw(AlertRaw raw) {
    this.raw = raw;
    raw.setAlert(this);
  }

  public AlarmType getType() {
    return type;
  }

  public void setType(AlarmType type) {
    this.type = type;
  }
}
