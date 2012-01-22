package org.inftel.tms.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Representa las alertas tal cual llegan de los dispositivos. Puede considerarse inecesario esta
 * entidad ya que el hecho de almacenarse como CLOB hace que el contenido no afecte al tamaño ni el
 * rendimiento de la tabla y portanto podria añadirse a Alert. Pero de esta forma se disminulle el
 * acoplamiento y facilita el uso de Alert (mmm este comentario debe mejorarse).
 *
 * @author ibaca
 */
@Entity(name = "alerts_raw")
public class AlertRaw extends BaseEntity {

  @OneToOne(optional = true, mappedBy = "raw", fetch = FetchType.LAZY)
  Alert alert;
  @Basic(optional = false, fetch = FetchType.EAGER)
  @Lob
  String rawData;

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
}
