package org.inftel.tms.domain;

import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Son los afectados, deben tener dispositivos, aunque no es un requisito del modelo. Los
 * dispositivos lanzan alertas que estaran asociadas al afectado propietario del dispositivo.
 *
 * Los afectados tambien tendran una lista de familiares a los que se podra contactar en caso de
 * necesidad.
 *
 * @author ibaca
 */
@Entity
@Table(name = "affected")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Affected.findAll", query = "SELECT a FROM Affected a"),
  @NamedQuery(name = "Affected.findById", query = "SELECT a FROM Affected a WHERE a.id = :id"),
  @NamedQuery(name = "Affected.findByCreated", query = "SELECT a FROM Affected a WHERE a.created = :created"),
  @NamedQuery(name = "Affected.findByUpdated", query = "SELECT a FROM Affected a WHERE a.updated = :updated"),
  @NamedQuery(name = "Affected.findByVersion", query = "SELECT a FROM Affected a WHERE a.version = :version")})
public class Affected extends BaseEntity {

  @OneToOne(fetch = FetchType.EAGER)
  Person data;
  @ManyToMany
  @JoinTable(name="affected_contacts")
  List<Person> contacts;
  @OneToMany(mappedBy="owner")
  public Set<Device> devices;

  public List<Person> getContacts() {
    return contacts;
  }

  public void setContacts(List<Person> contacts) {
    this.contacts = contacts;
  }

  public Person getData() {
    return data;
  }

  public void setData(Person data) {
    this.data = data;
  }
}
