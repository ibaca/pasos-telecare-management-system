package org.inftel.tms.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Representan las alertas recibidas de los dispositivos.
 *
 * Que info se guarda? Lo interesante es guardar un par de indices tipo/subtipo que permita hacer
 * busquedas rapidas. Ademas alguna columna booleana que ponga el estado abierto/cerrado, esto
 * independientemente de si se guarda un historico de acciones o intevenciones.
 *
 * @author ibaca
 */
@Entity(name = "alerts")
public class Alert extends BaseEntity {

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(unique = true, nullable = false, updatable = false)
  private AlertRaw raw;
}
