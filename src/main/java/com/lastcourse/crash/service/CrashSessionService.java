package com.lastcourse.crash.service;

import com.lastcourse.crash.exception.crashsession.CrashSessionNotFoundException;
import com.lastcourse.crash.model.crashsession.CrashSession;
import com.lastcourse.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.lastcourse.crash.model.crashsession.CrashSessionPostRequestBody;
import com.lastcourse.crash.model.entity.CrashSessionEntity;
import com.lastcourse.crash.repository.CrashSessionCacheRepository;
import com.lastcourse.crash.repository.CrashSessionEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CrashSessionService {

  @Autowired
  private CrashSessionEntityRepository crashSessionEntityRepository;

  @Autowired
  private SessionSpeakerService sessionSpeakerService;

  @Autowired
  private CrashSessionCacheRepository crashSessionCacheRepository;

  public List<CrashSession> getCrashSessions() {
    var crashSessions = crashSessionCacheRepository.getCrashSessionsListCache();
    if(!ObjectUtils.isEmpty(crashSessions)) {
      return crashSessions;
    }else {
      var crashSessionsList =  crashSessionEntityRepository.findAll().stream().map(CrashSession::from).toList(); // 비어있다면 데이터베이스 조회
      crashSessionCacheRepository.setCrashSessionsListCache(crashSessionsList); // 캐시 세팅
      return crashSessionsList;
    }
  }

  public CrashSession getCrashSessionBySessionId(Long sessionId) { // 단건 아이디 조회
    return crashSessionCacheRepository
        .getCrashSessionCache(sessionId)
        .orElseGet(() -> { // 캐시가 없으면 아래 로직 동작
          var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
          var crashSession = CrashSession.from(crashSessionEntity);
          crashSessionCacheRepository.setCrashSessionCache(crashSession);
          return crashSession;
        });

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

    if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())) {
      crashSessionEntity.setTitle(
          crashSessionPatchRequestBody.title()
      );
    }

    if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())) {
      crashSessionEntity.setBody(
          crashSessionPatchRequestBody.body()
      );
    }

    if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())) {
      crashSessionEntity.setCategory(
          crashSessionPatchRequestBody.category()
      );
    }

    if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())) {
      crashSessionEntity.setDateTime(
          crashSessionPatchRequestBody.dateTime()
      );
    }

    if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())) {
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
