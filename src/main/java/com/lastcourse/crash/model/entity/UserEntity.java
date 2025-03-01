package com.lastcourse.crash.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lastcourse.crash.model.user.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
    name = "\"user\"",
    indexes = {@Index(name = "user_username_idx", columnList = "username")
})
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column
  private ZonedDateTime createDateTime; // 유저의 생성 시점

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public ZonedDateTime getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(ZonedDateTime createDateTime) {
    this.createDateTime = createDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserEntity that = (UserEntity) o;
    return Objects.equals(userId, that.userId) && Objects.equals(username,
        that.username) && Objects.equals(password, that.password)
        && Objects.equals(name, that.name) && Objects.equals(email, that.email)
        && role == that.role && Objects.equals(createDateTime, that.createDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, username, password, name, email, role, createDateTime);
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if(this.role.equals(Role.ADMIN)) {
      return List.of(
          new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()), // hasRole에도 적용을 시키려면 "ROLE_"처럼 PreFix가 붙어야함
          new SimpleGrantedAuthority(Role.ADMIN.name()), // hasAuthority 권한 부여
          new SimpleGrantedAuthority("ROLE_" + Role.USER.name()),
          new SimpleGrantedAuthority(Role.USER.name()));
    }else {
      return List.of(
          new SimpleGrantedAuthority("ROLE_" + Role.USER.name()),
          new SimpleGrantedAuthority(Role.USER.name()));
    }
  } // 해당 유저의 부여되어 있는 권한의 목록을 리턴함

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }

  public static UserEntity of(String username, String password, String name, String email) {
    var userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setPassword(password);
    userEntity.setName(name);
    userEntity.setEmail(email);
    userEntity.setRole(Role.USER);
    return userEntity;
  }

  @PrePersist
  private void prePersist() {
    this.createDateTime = ZonedDateTime.now();
  }
}
