package org.inftel.tms.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
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
  private String fullName;
  private String nickname;
  @Column(nullable = false, unique = true)
  private String email;
  private String password;
  private Role userRole;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
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
