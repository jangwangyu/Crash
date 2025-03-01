package com.lastcourse.crash.exception.sessionspeaker;

import com.lastcourse.crash.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class SessionSpeakerNotFoundException extends ClientErrorException {
  public SessionSpeakerNotFoundException() {
    super(HttpStatus.NOT_FOUND, "SessionSpeaker not found");
  }

  public SessionSpeakerNotFoundException(Long speakerId) {
    super(HttpStatus.NOT_FOUND, "SessionSpeaker with speakerId " + speakerId + " not found");
  }
}
