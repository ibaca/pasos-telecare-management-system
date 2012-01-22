package org.inftel.tms.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Representa a los dispositivos asiciados al sistema. Cada afectado debe tener almenos un
 * dispositivo activo. Los dispositivos deben delegar las posiciones recibidas al afectado.
 *
 * @author ibaca
 */
@Entity(name = "devices")
public class Device extends BaseEntity {

  private String mobileNumber;
  private String simCard;
  private Integer batery;
  @ManyToOne
  @JoinColumn(nullable = true) // aunq no es lo normal, puede ser nulo!
  private Affected owner;

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
