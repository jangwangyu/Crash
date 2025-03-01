package com.lastcourse.crash.exception.user;

import com.lastcourse.crash.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {
  public UserNotFoundException() {
    super(HttpStatus.NOT_FOUND, "User not found");
  }

  public UserNotFoundException(String username) {
    super(HttpStatus.NOT_FOUND, "User with username " + username + " not found");
  }
}
