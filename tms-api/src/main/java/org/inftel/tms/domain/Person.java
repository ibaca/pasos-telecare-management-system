package org.inftel.tms.domain;

import java.util.List;
import javax.persistence.*;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Las personas registradas en el sistema. Pueden ser tanto afectados como falmiliares.
 *
 * @author ibaca
 */
@Entity(name = "people")
public class Person extends BaseEntity {

  private String firstName;
  private String lastName;
  @Basic(optional = false)
  private String email;
  private String mobileNumber;
  private double latitude;
  private double longitude;
  @ManyToMany(mappedBy = "contacts", fetch = FetchType.LAZY)
  private List<Affected> chargeOf;
  @OneToOne(mappedBy = "data", optional = true, fetch = FetchType.LAZY)
  private Affected affected;

  public Affected getAffected() {
    return affected;
  }

  public void setAffected(Affected affected) {
    this.affected = affected;
  }

  public List<Affected> getChargeOf() {
    return chargeOf;
  }

  public void setChargeOf(List<Affected> chargeOf) {
    this.chargeOf = chargeOf;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public boolean isAffected() {
    return getAffected() == null;
  }

  public String getSimpleName() {
    return (isBlank(getFirstName())) ? getEmail() : (getFirstName() + " " + getLastName()).trim();
  }
}
