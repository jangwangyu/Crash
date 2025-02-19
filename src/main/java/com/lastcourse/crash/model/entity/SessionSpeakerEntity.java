package com.lastcourse.crash.model.entity;

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
import java.util.Objects;
import java.util.Random;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "sessionspeaker")
public class SessionSpeakerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long speakerId;

  @Column(nullable = false)
  private String company;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT",nullable = false)
  private String description;

  @Column(nullable = false)
  private String profile;

  public Long getSpeakerId() {
    return speakerId;
  }

  public void setSpeakerId(Long speakerId) {
    this.speakerId = speakerId;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionSpeakerEntity that = (SessionSpeakerEntity) o;
    return Objects.equals(speakerId, that.speakerId) && Objects.equals(company,
        that.company) && Objects.equals(name, that.name) && Objects.equals(
        description, that.description) && Objects.equals(profile, that.profile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(speakerId, company, name, description, profile);
  }

  public static SessionSpeakerEntity of(String company, String name, String description) {
    var sessionSpeakerEntity = new SessionSpeakerEntity();
    sessionSpeakerEntity.setCompany(company);
    sessionSpeakerEntity.setName(name);
    sessionSpeakerEntity.setDescription(description);
    sessionSpeakerEntity.setProfile(
        "https://dev-jayce.github.io/public/profile/" + (new Random().nextInt(100) +1) + ".png");
    return sessionSpeakerEntity;
  }

}
