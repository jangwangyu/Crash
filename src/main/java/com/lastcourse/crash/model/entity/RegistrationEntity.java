package com.lastcourse.crash.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "registration",
    indexes = {
        @Index(
            name = "registration_userid_sessionid_idx",
            columnList = "userid, sessionid",
            unique = true
        )
    }

)
public class RegistrationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long registrationId;

  @ManyToOne
  @JoinColumn(name = "userid")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "sessionid")
  private CrashSessionEntity session;

  @Column
  private ZonedDateTime createdDateTime;

  public Long getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(Long registrationId) {
    this.registrationId = registrationId;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public CrashSessionEntity getSession() {
    return session;
  }

  public void setSession(CrashSessionEntity session) {
    this.session = session;
  }

  public ZonedDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(ZonedDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrationEntity that = (RegistrationEntity) o;
    return Objects.equals(registrationId, that.registrationId) && Objects.equals(
        user, that.user) && Objects.equals(session, that.session)
        && Objects.equals(createdDateTime, that.createdDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registrationId, user, session, createdDateTime);
  }

  public static RegistrationEntity of(UserEntity userEntity, CrashSessionEntity sessionEntity) {
    var registrationEntity = new RegistrationEntity();
    registrationEntity.setUser(userEntity);
    registrationEntity.setSession(sessionEntity);
    return registrationEntity;
  }

  @PrePersist
  private void prePersist() {
    this.createdDateTime = ZonedDateTime.now();
  }
}
