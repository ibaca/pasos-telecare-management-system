package org.inftel.tms.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa las alertas tal cual llegan de los dispositivos. Puede considerarse inecesario esta
 * entidad ya que el hecho de almacenarse como CLOB hace que el contenido no afecte al tamaño ni el
 * rendimiento de la tabla y portanto podria añadirse a Alert. Pero de esta forma se disminulle el
 * acoplamiento y facilita el uso de Alert (mmm este comentario debe mejorarse).
 *
 * @author ibaca
 */
@Entity
@Table(name = "alerts_raw", catalog = "", schema = "tms")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AlertRaw.findAll", query = "SELECT a FROM AlertRaw a"),
  @NamedQuery(name = "AlertRaw.findById", query = "SELECT a FROM AlertRaw a WHERE a.id = :id"),
  @NamedQuery(name = "AlertRaw.findByCreated", query = "SELECT a FROM AlertRaw a WHERE a.created = :created"),
  @NamedQuery(name = "AlertRaw.findByOrigin", query = "SELECT a FROM AlertRaw a WHERE a.origin = :origin"),
  @NamedQuery(name = "AlertRaw.findByUpdated", query = "SELECT a FROM AlertRaw a WHERE a.updated = :updated"),
  @NamedQuery(name = "AlertRaw.findByVersion", query = "SELECT a FROM AlertRaw a WHERE a.version = :version")})
public class AlertRaw extends BaseEntity {

  @OneToOne(optional = true, mappedBy = "raw", fetch = FetchType.LAZY)
  Alert alert;
  /**
   * Cadena que representa el origen de la alerta, actualmente el telefono del dispositivo
   */
  @Basic(optional = false)
  String origin;
  @Basic(optional = false, fetch = FetchType.EAGER)
  @Lob
  String rawData;

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Alert getAlert() {
    return alert;
  }

  public void setAlert(Alert alert) {
    this.alert = alert;
  }

  public String getRawData() {
    return rawData;
  }

  public void setRawData(String rawData) {
    this.rawData = rawData;
  }
  
  public boolean isProcessed() {
    return getAlert() == null;
  }
}
