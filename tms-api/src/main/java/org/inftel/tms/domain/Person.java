package org.inftel.tms.domain;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Las personas registradas en el sistema. Pueden ser tanto afectados como falmiliares.
 *
 * @author ibaca
 */
@Entity
@Table(name = "people")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
  @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id"),
  @NamedQuery(name = "Person.findByCreated", query = "SELECT p FROM Person p WHERE p.created = :created"),
  @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
  @NamedQuery(name = "Person.findByFirstname", query = "SELECT p FROM Person p WHERE p.firstName = :firstname"),
  @NamedQuery(name = "Person.findByLastname", query = "SELECT p FROM Person p WHERE p.lastName = :lastname"),
  @NamedQuery(name = "Person.findByLatitude", query = "SELECT p FROM Person p WHERE p.latitude = :latitude"),
  @NamedQuery(name = "Person.findByLongitude", query = "SELECT p FROM Person p WHERE p.longitude = :longitude"),
  @NamedQuery(name = "Person.findByMobileNumber", query = "SELECT p FROM Person p WHERE p.mobileNumber = :mobilenumber"),
  @NamedQuery(name = "Person.findByUpdated", query = "SELECT p FROM Person p WHERE p.updated = :updated"),
  @NamedQuery(name = "Person.findByVersion", query = "SELECT p FROM Person p WHERE p.version = :version")})
public class Person extends BaseEntity {

  private String firstName;
  private String lastName;
  @Basic(optional = false)
  @Column(nullable = false, unique = true)
  private String email;
  private String mobileNumber;
  @Latitude
  private double latitude;
  @Longitude
  private double longitude;
  @Geocells
  @ElementCollection
  private List<String> geoCellsData = new ArrayList<String>();
  @ManyToMany(mappedBy = "contacts", fetch = FetchType.LAZY)
  private List<Affected> chargeOf;
  @OneToOne(mappedBy = "data", optional = true, fetch = FetchType.LAZY)
  private Affected affected;

  public List<String> getGeoCellsData() {
    return geoCellsData;
  }

  public void setGeoCellsData(List<String> geoCellsData) {
    this.geoCellsData = geoCellsData;
  }

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
