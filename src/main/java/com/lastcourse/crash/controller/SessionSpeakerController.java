package com.lastcourse.crash.controller;

import com.lastcourse.crash.model.sessionspeaker.SessionSpeaker;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.lastcourse.crash.model.user.User;
import com.lastcourse.crash.model.user.UserAuthenticationResponse;
import com.lastcourse.crash.model.user.UserLoginRequestBody;
import com.lastcourse.crash.model.user.UserSignUpRequestBody;
import com.lastcourse.crash.service.SessionSpeakerService;
import com.lastcourse.crash.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/session-speakers")
public class SessionSpeakerController {
  @Autowired
  private SessionSpeakerService sessionSpeakerService;


  @GetMapping
  public ResponseEntity<List<SessionSpeaker>> getSessionSpeakers() {
    var sesisonSpeakers = sessionSpeakerService.getSessionSpeakers();
    return ResponseEntity.ok(sesisonSpeakers);
  }


  @GetMapping("/{speakerId}")
  public ResponseEntity<SessionSpeaker> getSessionSpeakerBySpeakerId(
      @PathVariable Long speakerId) {
    var sesisonSpeaker = sessionSpeakerService.getSessionSpeakerBySpeakerId(speakerId);
    return ResponseEntity.ok(sesisonSpeaker);
  }


  @PostMapping
  public ResponseEntity<SessionSpeaker> createSessionSpeaker(
      @Valid @RequestBody SessionSpeakerPostRequestBody sessionSpeakerPostRequestBody
  ) {
    var sesisonSpeaker = sessionSpeakerService.createSessionSpeaker(sessionSpeakerPostRequestBody);
    return ResponseEntity.ok(sesisonSpeaker);
  }

  @PatchMapping("/{speakerId}")
  public ResponseEntity<SessionSpeaker> updateSessionSpeaker(
      @Valid @RequestBody SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody,
      @PathVariable Long speakerId) {
    var updateSessionSpeaker = sessionSpeakerService.updateSessionSpeaker(speakerId, sessionSpeakerPatchRequestBody);
    return ResponseEntity.ok(updateSessionSpeaker);
  }

  @DeleteMapping("/{speakerId}")
  public ResponseEntity<Void> deleteSessionSpeaker(
      @PathVariable Long speakerId) {
    sessionSpeakerService.deleteSessionSpeaker(speakerId);
    return ResponseEntity.noContent().build();
  }
}
