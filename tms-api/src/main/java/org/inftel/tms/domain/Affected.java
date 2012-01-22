package org.inftel.tms.domain;

import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Son los afectados, deben tener dispositivos, aunque no es un requisito del modelo. Los
 * dispositivos lanzan alertas que estaran asociadas al afectado propietario del dispositivo.
 *
 * Los afectados tambien tendran una lista de familiares a los que se podra contactar en caso de
 * necesidad.
 *
 * @author ibaca
 */
@Entity(name="affected")
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
