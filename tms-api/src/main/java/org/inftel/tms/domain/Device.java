/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.domain;

import javax.persistence.Entity;

/**
 *
 * @author ibaca
 */
@Entity(name="devices")
public class Device extends BaseEntity {

  private String mobileNumber;
  private String simCard;
  private Integer batery;

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
