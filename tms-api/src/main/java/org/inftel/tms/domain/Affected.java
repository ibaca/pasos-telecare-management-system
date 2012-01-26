package org.inftel.tms.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
        @NamedQuery(name = "Affected.findByVersion", query = "SELECT a FROM Affected a WHERE a.version = :version"),
        @NamedQuery(name = "Affected.countByType", query = "SELECT count(a) FROM Affected a WHERE a.type = :type") })
public class Affected extends BaseEntity {

    private static final long serialVersionUID = -8798996677273198603L;
    @ManyToMany
    @JoinTable(name = "affected_contacts")
    private List<Person> contacts;
    @OneToOne(fetch = FetchType.EAGER)
    private Person data;
    @OneToMany(mappedBy = "owner")
    private Set<Device> devices;
    private AffectedType type;

    public List<Person> getContacts() {
        return contacts;
    }

    public Person getData() {
        return data;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public AffectedType getType() {
        return type;
    }

    public void setContacts(List<Person> contacts) {
        this.contacts = contacts;
    }

    public void setData(Person data) {
        this.data = data;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public void setType(AffectedType type) {
        this.type = type;
    }
}
