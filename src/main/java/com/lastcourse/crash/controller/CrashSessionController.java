package com.lastcourse.crash.controller;

import com.lastcourse.crash.model.crashsession.CrashSession;
import com.lastcourse.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.lastcourse.crash.model.crashsession.CrashSessionPostRequestBody;
import com.lastcourse.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.lastcourse.crash.model.entity.UserEntity;
import com.lastcourse.crash.service.CrashSessionService;
import com.lastcourse.crash.service.RegistrationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crash-sessions")
public class CrashSessionController {
  @Autowired
  private CrashSessionService crashSessionService;
  @Autowired
  private RegistrationService registrationService;


  @GetMapping
  public ResponseEntity<List<CrashSession>> getCrashSessions() {
    var crashSessions = crashSessionService.getCrashSessions();
    return ResponseEntity.ok(crashSessions);
  }


  @GetMapping("/{sessionId}")
  public ResponseEntity<CrashSession> getCrashSessionBySessionId(
      @PathVariable Long sessionId) {
    var crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
    return ResponseEntity.ok(crashSession);
  }

  @GetMapping("/{sessionId}/registration-status")
  public ResponseEntity<CrashSessionRegistrationStatus> getCrashSessionRegistrationStatusBySessionId(
      @PathVariable Long sessionId, Authentication authentication) {

    var registrationStatus =
        registrationService.getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(sessionId,(UserEntity) authentication.getPrincipal());
    var crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
    return ResponseEntity.ok(registrationStatus);
  }


  @PostMapping
  public ResponseEntity<CrashSession> createCrashSession(
      @Valid @RequestBody CrashSessionPostRequestBody crashSessionPostRequestBody
  ) {
    var crashSession = crashSessionService.createCrashSession(crashSessionPostRequestBody);
    return ResponseEntity.ok(crashSession);
  }

  @PatchMapping("/{sessionId}")
  public ResponseEntity<CrashSession> updateCrashSession(
      @Valid @RequestBody CrashSessionPatchRequestBody crashSessionPatchRequestBody,
      @PathVariable Long sessionId) {
    var crashSession = crashSessionService.updateCrashSession(sessionId, crashSessionPatchRequestBody);
    return ResponseEntity.ok(crashSession);
  }

  @DeleteMapping("/{sessionId}")
  public ResponseEntity<Void> deleteCrashSession(
      @PathVariable Long sessionId) {
    crashSessionService.deleteCrashSession(sessionId);
    return ResponseEntity.noContent().build();
  }
}
