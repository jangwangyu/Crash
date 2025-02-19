package com.lastcourse.crash.service;

import com.lastcourse.crash.exception.sessionspeaker.SessionSpeakerNotFoundException;
import com.lastcourse.crash.model.entity.SessionSpeakerEntity;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeaker;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.lastcourse.crash.repository.SessionSpeakerEntityRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SessionSpeakerService {
  @Autowired
  private SessionSpeakerEntityRepository sessionSpeakerEntityRepository;

  public List<SessionSpeaker> getSessionSpeakers() {
    var sessionSpeakerEntities = sessionSpeakerEntityRepository.findAll();
    return sessionSpeakerEntities.stream().map(SessionSpeaker::from).toList();
  }

  public SessionSpeakerEntity getSessionSpeakerEntityBySpeakerId(Long speakerId) {
    return sessionSpeakerEntityRepository
        .findById(speakerId)
        .orElseThrow(() -> new SessionSpeakerNotFoundException(speakerId)
        );
  } // id찾기

  public SessionSpeaker getSessionSpeakerBySpeakerId(Long speakerId) {
   var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
   return SessionSpeaker.from(sessionSpeakerEntity);
  }

  public SessionSpeaker createSessionSpeaker(
      @Valid SessionSpeakerPostRequestBody sessionSpeakerPostRequestBody) {
    var sessionSpeakerEntity =
        SessionSpeakerEntity.of(
            sessionSpeakerPostRequestBody.company(),
            sessionSpeakerPostRequestBody.name(),
            sessionSpeakerPostRequestBody.description()
    );
    return SessionSpeaker.from(sessionSpeakerEntityRepository.save(sessionSpeakerEntity));
  }

  public SessionSpeaker updateSessionSpeaker(
      Long speakerId,
      @Valid SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody) {
    var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);

    if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.company())) {
      sessionSpeakerEntity.setCompany(sessionSpeakerPatchRequestBody.company());
    }

    if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.name())) {
      sessionSpeakerEntity.setName(sessionSpeakerPatchRequestBody.name());
    }

    if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.description())) {
      sessionSpeakerEntity.setDescription(sessionSpeakerPatchRequestBody.description());
    }

    return SessionSpeaker.from(sessionSpeakerEntityRepository.save(sessionSpeakerEntity)); // 수정된 Entity는 sessionSpeakerEntityRepository.save를 통해 저장한 후 SessionSpeaker.from 레코드로 변환해서 클라이언트에 응답
  }

  public void deleteSessionSpeaker(Long speakerId) {
    var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
    sessionSpeakerEntityRepository.delete(sessionSpeakerEntity);
  }
}
