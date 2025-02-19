package com.lastcourse.crash.model.sessionspeaker;

import com.lastcourse.crash.model.entity.SessionSpeakerEntity;

public record SessionSpeaker(Long speakerId, String compony, String name, String description, String profile) {
  public static SessionSpeaker from(SessionSpeakerEntity sessionSpeakerEntity) {
    return new SessionSpeaker(
        sessionSpeakerEntity.getSpeakerId(),
        sessionSpeakerEntity.getCompany(),
        sessionSpeakerEntity.getName(),
        sessionSpeakerEntity.getDescription(),
        sessionSpeakerEntity.getProfile()
    );
  }
}
