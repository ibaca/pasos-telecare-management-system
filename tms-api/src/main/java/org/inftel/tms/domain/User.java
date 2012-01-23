package org.inftel.tms.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Usuarios del sistema. Se usan para acceder a la pagina web y poder gestionar a los afectados y
 * monitorizar y reaccionar ante las alertas que surgan.
 *
 * @author ibaca
 */
@Entity
@Table(name = "users", catalog = "", schema = "tms")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
  @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
  @NamedQuery(name = "User.findByCreated", query = "SELECT u FROM User u WHERE u.created = :created"),
  @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
  @NamedQuery(name = "User.findByFullname", query = "SELECT u FROM User u WHERE u.fullName = :fullname"),
  @NamedQuery(name = "User.findByNickname", query = "SELECT u FROM User u WHERE u.nickname = :nickname"),
  @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
  @NamedQuery(name = "User.findByUpdated", query = "SELECT u FROM User u WHERE u.updated = :updated"),
  @NamedQuery(name = "User.findByUserrole", query = "SELECT u FROM User u WHERE u.userRole = :userrole"),
  @NamedQuery(name = "User.findByVersion", query = "SELECT u FROM User u WHERE u.version = :version")})
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
