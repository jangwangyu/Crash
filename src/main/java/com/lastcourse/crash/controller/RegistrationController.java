package com.lastcourse.crash.controller;

import com.lastcourse.crash.model.crashsession.CrashSession;
import com.lastcourse.crash.model.entity.UserEntity;
import com.lastcourse.crash.model.registration.Registration;
import com.lastcourse.crash.model.registration.RegistrationPostRequestBody;
import com.lastcourse.crash.service.RegistrationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {

  @Autowired
  private RegistrationService registrationService;


  @GetMapping
  public ResponseEntity<List<Registration>> getRegistrations(Authentication authentication) {
    var registrations = registrationService.getRegistrationsByCurrentUser(
        (UserEntity) authentication.getPrincipal()
    );
    return ResponseEntity.ok(registrations);
  }


  @GetMapping("/{registrationId}")
  public ResponseEntity<Registration> getRegistrationByRegistrationId(
      @PathVariable Long registrationId, Authentication authentication) {
    var registration = registrationService.getRegistrationByRegistrationIdByCurrentUser(
        registrationId, (UserEntity) authentication.getPrincipal()
    );
    return ResponseEntity.ok(registration);
  }


  @PostMapping
  public ResponseEntity<Registration> createRegistration(
      @Valid @RequestBody RegistrationPostRequestBody registrationPostRequestBody,
      Authentication authentication
  ) {
    var registration = registrationService.createRegistrationByCurrentUser(
        registrationPostRequestBody, (UserEntity) authentication.getPrincipal()
    );
    return ResponseEntity.ok(registration);
  }

  @DeleteMapping("/{registrationId}")
  public ResponseEntity<Void> deleteRegistration(
      @PathVariable Long registrationId, Authentication authentication) {
    registrationService.deleteRegistrationByRegistrationIdByCurrentUser(
        registrationId, (UserEntity) authentication.getPrincipal()
    );
    return ResponseEntity.noContent().build();
  }
}
