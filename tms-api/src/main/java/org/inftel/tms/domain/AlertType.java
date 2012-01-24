package org.inftel.tms.domain;

/**
 * Tipo de alarma, directamente relacionado con el protocolo paSOS.
 *
 * FIXME FIXME no puede cambiarse el orden del enumerado
 * http://duydo.com/effective-jpa-persist-an-enumerationeffectively/
 * 
 * @author ibaca
 */
public enum AlertType {

  /**
   * Alerta tipo usuario, es decir, alarma que se ha generado explicitamente por el usuario pulsando
   * o activando el servicio de alarmas.
   */
  USER,
  /**
   * Alerta de dispositivo. Generada automaticamente por el dispositivo debido a algun criterio
   * interno como por ejemplo un valor elevado del acelerometros o una temeratura de riesgo para el
   * individuo.
   */
  DEVICE,
  /**
   * Alerta generada por el dispositivo, pero directamente relacionada con este y no con el
   * afectado. Estas alertas pueden ser del tipo bateria baja o baja cobertura del dispositivo.
   */
  TECHNICAL
}