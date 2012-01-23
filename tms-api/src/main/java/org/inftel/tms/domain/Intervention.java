/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa una reaccion ante una alerta por un usuario del sistema de teleasistencia. Por ahora
 * se da por supuesto que tras añadir una intervencion, la alerta queda en estado cerrado. Pero en
 * un futuro debe contemplarse un flujo de intervenciones e incluso un tipo de intervencion que sea
 * reabrir una alerta.
 *
 * @author ibaca
 */
@Entity
@Table(name = "interventions")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Intervention.findAll", query = "SELECT i FROM Intervention i"),
  @NamedQuery(name = "Intervention.findById", query = "SELECT i FROM Intervention i WHERE i.id = :id"),
  @NamedQuery(name = "Intervention.findByCreated", query = "SELECT i FROM Intervention i WHERE i.created = :created"),
  @NamedQuery(name = "Intervention.findByDescription", query = "SELECT i FROM Intervention i WHERE i.description = :description"),
  @NamedQuery(name = "Intervention.findByUpdated", query = "SELECT i FROM Intervention i WHERE i.updated = :updated"),
  @NamedQuery(name = "Intervention.findByVersion", query = "SELECT i FROM Intervention i WHERE i.version = :version")})
public class Intervention extends BaseEntity {

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private Alert alert;
  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, updatable = false)
  private User by;
  private String description;

  public Alert getAlert() {
    return alert;
  }

  public void setAlert(Alert alert) {
    this.alert = alert;
  }

  public User getBy() {
    return by;
  }

  public void setBy(User by) {
    this.by = by;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
