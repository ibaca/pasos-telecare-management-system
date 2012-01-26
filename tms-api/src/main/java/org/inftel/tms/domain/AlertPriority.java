package org.inftel.tms.domain;

/**
 * Inidica la prioridad de la alerta. Todas las alertas son importantes en un sistema de
 * teleasistenca, pero bajo situaciones de saturacion debe poder organizarse las mas prioritarias.
 * Ademas presentan datos utiles a la hora de realizar calculos estadisticos.
 *
 * @author ibaca
 *
 */
public enum AlertPriority {

  /**
   * Indica una alerta critica que no puede aplazarse su intervencion. La mayoria de las alertas de
   * {@link AlertType#USER} son de tipo critico.
   */
  CRITICAL,
  /**
   * Indica una alerta importante, pero que no asegura una situacion critica para el afectado. Las
   * alertas {@link AlertType#DEVICE} suelen ser de tipo importante.
   */
  IMPORTANT,
  /**
   * Indican una alerta que no es prioritaria de intervenir de forma inmediata. Las alertas
   * {@link AlertType#TECHNICAL} suelen ser de tipo normal.
   */
  NORMAL,
  /**
   * Algunos mensajes del protocolo pasos no estan directamente relacionados con alertas de usuario
   * como por ejemplo el envio de posiciones. Estas alertas se consideran de tipo informativo y no
   * tienen porque ser intervenidas.
   */
  INFO,
  /**
   * Este estado se usa cuando se reciben alertas falsas. El estado normalmente debe ser establecido
   * por el interventor, y su principal funcion es mantener un registro de falsas alertas para temas
   * estadisticos.
   */
  FALSE;
}
