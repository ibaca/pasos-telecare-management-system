/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa una reaccion ante una alerta por un usuario del sistema de teleasistencia. Por ahora
 * se da por supuesto que tras añadir una intervencion, la alerta queda en estado cerrado. Pero en
 * un futuro debe contemplarse un flujo de intervenciones e incluso un tipo de intervencion que sea
 * reabrir una alerta.
 * 
 * @author ibaca
 */
@Entity
@Table(name = "interventions")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Intervention.findAll", query = "SELECT i FROM Intervention i"),
        @NamedQuery(name = "Intervention.findById", query = "SELECT i FROM Intervention i WHERE i.id = :id"),
        @NamedQuery(name = "Intervention.findByCreated", query = "SELECT i FROM Intervention i WHERE i.created = :created"),
        @NamedQuery(name = "Intervention.findByDescription", query = "SELECT i FROM Intervention i WHERE i.description = :description"),
        @NamedQuery(name = "Intervention.findByUpdated", query = "SELECT i FROM Intervention i WHERE i.updated = :updated"),
        @NamedQuery(name = "Intervention.findByVersion", query = "SELECT i FROM Intervention i WHERE i.version = :version"),
        @NamedQuery(name = Intervention.COUNT_BY_TYPE, query = "SELECT count(i) FROM Intervention i WHERE i.by = :user AND i.created BETWEEN :fromDate AND :toDate") })
public class Intervention extends BaseEntity {

    public static final String COUNT_BY_TYPE = "Intervention.countByType";

    private static final long serialVersionUID = 27387751321069701L;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Alert alert;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User by;

    private String description;

    /**
     * Alerta por la cual se realizo la intervencion.
     * 
     * @return alerta intervenida
     */
    public Alert getAlert() {
        return alert;
    }

    /**
     * Usuario que realizo la intervencion.
     * 
     * @return usuario que realizo la intervencion
     */
    public User getBy() {
        return by;
    }

    /**
     * Descripcion de la intervencion llevada a cabo.
     * 
     * @return descripcion de la intervencion
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la alerta por la cual se va ha realizar la intervencion.
     * 
     * @param alert
     *            alerta asociada
     */
    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    /**
     * Establece el usuario que esta realizando la internvencion.
     * 
     * @param by
     *            usuario que realiza la intervencion
     */
    public void setBy(User by) {
        this.by = by;
    }

    /**
     * Descripcion de la intervencion realizada.
     * 
     * @param description
     *            descripcion de la intervencion
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
