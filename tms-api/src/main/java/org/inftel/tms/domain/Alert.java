package org.inftel.tms.domain;

import java.util.List;
import javax.persistence.*;

/**
 * Representan las alertas recibidas de los dispositivos.
 *
 * Que info se guarda? Lo interesante es guardar un par de indices tipo/subtipo que permita hacer
 * busquedas rapidas. Ademas alguna columna booleana que ponga el estado abierto/cerrado, esto
 * independientemente de si se guarda un historico de acciones o intevenciones.
 *
 * @author ibaca
 */
@Entity(name = "alerts")
public class Alert extends BaseEntity {

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
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(unique = true, nullable = false, updatable = false)
  private AlertRaw raw;
  @OneToMany(mappedBy = "alert", cascade= CascadeType.ALL)
  private List<Intervention> interventions;

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
  }

  public AlarmType getType() {
    return type;
  }

  public void setType(AlarmType type) {
    this.type = type;
  }
}
