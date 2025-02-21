package com.lastcourse.crash.service;

import com.lastcourse.crash.exception.crashsession.CrashSessionNotFoundException;
import com.lastcourse.crash.model.crashsession.CrashSession;
import com.lastcourse.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.lastcourse.crash.model.crashsession.CrashSessionPostRequestBody;
import com.lastcourse.crash.model.entity.CrashSessionEntity;
import com.lastcourse.crash.model.entity.SessionSpeakerEntity;
import com.lastcourse.crash.repository.CrashSessionEntityRepository;
import com.lastcourse.crash.repository.SessionSpeakerEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CrashSessionService {

  @Autowired
  private CrashSessionEntityRepository crashSessionEntityRepository;

  @Autowired
  private SessionSpeakerService sessionSpeakerService;

  public List<CrashSession> getCrashSessions() {
    return crashSessionEntityRepository.findAll().stream().map(CrashSession::from).toList();
  }

  public CrashSession getCrashSessionBySessionId(Long sessionId) { // 단건 아이디 조회
    var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
    return CrashSession.from(crashSessionEntity);
  }

  public CrashSession createCrashSession(
      CrashSessionPostRequestBody crashSessionPostRequestBody) {
    var sessionSpeakerEntity =
        sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
            crashSessionPostRequestBody.speakerId());

    var crashSessionEntity =
        CrashSessionEntity.of(
            crashSessionPostRequestBody.title(),
            crashSessionPostRequestBody.body(),
            crashSessionPostRequestBody.category(),
            crashSessionPostRequestBody.dateTime(),
            sessionSpeakerEntity
        );

    return CrashSession.from(
        crashSessionEntityRepository.save(crashSessionEntity)
    );
  }

  public CrashSession updateCrashSession(
      Long sessionId, CrashSessionPatchRequestBody crashSessionPatchRequestBody) {
    var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);

    if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())){
      crashSessionEntity.setTitle(
          crashSessionPatchRequestBody.title()
      );
    }

    if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())){
      crashSessionEntity.setBody(
          crashSessionPatchRequestBody.body()
      );
    }

    if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())){
      crashSessionEntity.setCategory(
          crashSessionPatchRequestBody.category()
      );
    }

    if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())){
      crashSessionEntity.setDateTime(
          crashSessionPatchRequestBody.dateTime()
      );
    }

    if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())){
      var sessionSpeakerEntity = sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
          crashSessionPatchRequestBody.speakerId()
      );
      crashSessionEntity.setSpeaker(sessionSpeakerEntity);

    }

    return CrashSession.from(
        crashSessionEntityRepository.save(crashSessionEntity)
    );
  }

  public void deleteCrashSession(
      Long sessionId) {
    var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
    crashSessionEntityRepository.delete(crashSessionEntity);
  }

  public CrashSessionEntity getCrashSessionEntityBySessionId(Long sessionId) { // 단건 검색 로직
    return crashSessionEntityRepository.findById(sessionId).orElseThrow(
        () -> new CrashSessionNotFoundException(sessionId)
    );
  }
}
