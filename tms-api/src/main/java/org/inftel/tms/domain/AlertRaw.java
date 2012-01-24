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
@Table(name = "alerts_raw")
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
  private Alert alert;
  @Basic(optional = false)
  private String origin;
  @Basic(optional = false, fetch = FetchType.EAGER)
  @Lob
  private String rawData;

  /**
   * Cadena que representa el origen de la alerta, actualmente el telefono del dispositivo. Se usa
   * para encontrar el {@link Device} que origino esta alerta.
   *
   * @return identificador que representa el origen de la alerta
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Modifica el origen de la alerta. Este campo es obligatorio.
   *
   * @param origin cadena que representa el origen de la alerta
   * @see #getOrigin()
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * Alerta que representa la informacion parseada. Si el mensaje es parseable, la alerta debe
   * existir.
   *
   * @return la alerta asociada
   */
  public Alert getAlert() {
    return alert;
  }

  /**
   * Establece la alerta asociada. Debe llamarse tras parsear el raw.
   *
   * @param alert alerta parseada del contenido raw
   */
  public void setAlert(Alert alert) {
    this.alert = alert;
  }

  /**
   * Contenido sin parsear del mensaje de alerta.
   *
   * @return el contenido sin parsear del mensaje de alera
   */
  public String getRawData() {
    return rawData;
  }

  /**
   * Establece el contenido sin parsear del mensaje de alerta. Este campo es obligatorio.
   *
   * @param rawData mensaje de alerta sin parsear
   */
  public void setRawData(String rawData) {
    this.rawData = rawData;
  }

  public boolean isProcessed() {
    return getAlert() == null;
  }
}
