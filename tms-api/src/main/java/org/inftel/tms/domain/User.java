package org.inftel.tms.domain;

import javax.persistence.Entity;

/**
 * Usuarios del sistema. Se usan para acceder a la pagina web y poder gestionar a los afectados y
 * monitorizar y reaccionar ante las alertas que surgan.
 *
 * @author ibaca
 */
@Entity(name = "users")
public class User extends BaseEntity {

  public static enum Role {

    USER, ADMIN, ROOT
  };
  private String name;
  private String password;
  private Role userRole;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getUserRole() {
    return userRole;
  }

  public void setUserRole(Role userRole) {
    this.userRole = userRole;
  }
}
