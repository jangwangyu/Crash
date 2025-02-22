package com.lastcourse.crash.service;

import com.lastcourse.crash.exception.registration.RegistrationAlreadyExistsException;
import com.lastcourse.crash.exception.registration.RegistrationNotFoundException;
import com.lastcourse.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.lastcourse.crash.model.entity.RegistrationEntity;
import com.lastcourse.crash.model.entity.UserEntity;
import com.lastcourse.crash.model.registration.Registration;
import com.lastcourse.crash.model.registration.RegistrationPostRequestBody;
import com.lastcourse.crash.repository.RegistrationEntityRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

  @Autowired
  private RegistrationEntityRepository registrationEntityRepository;

  @Autowired
  private CrashSessionService crashSessionService;

  public List<Registration> getRegistrationsByCurrentUser(UserEntity currentUser) {
    var registrationEntities = registrationEntityRepository.findByUser(currentUser);
    return registrationEntities.stream().map(Registration::from).toList();
  }

  public Registration getRegistrationByRegistrationIdByCurrentUser(Long registrationId,
      UserEntity currentUser) {
    var registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
    return Registration.from(registrationEntity);
  }

  public RegistrationEntity getRegistrationEntityByRegistrationIdAndUserEntity(Long registrationId,
      UserEntity currentUser) {
    return registrationEntityRepository.findByRegistrationIdAndUser(registrationId, currentUser)
        .orElseThrow(
            () -> new RegistrationNotFoundException(registrationId, currentUser)
        );
  }

  public Registration createRegistrationByCurrentUser(
      @Valid RegistrationPostRequestBody registrationPostRequestBody, UserEntity currentUser) {

    var crashSessionEntity =
        crashSessionService
            .getCrashSessionEntityBySessionId(registrationPostRequestBody.sessionId());

    registrationEntityRepository
        .findByUserAndSession(currentUser, crashSessionEntity).ifPresent(
            registrationEntity -> {
              throw new RegistrationAlreadyExistsException(
                  registrationEntity.getRegistrationId(), currentUser
              );
            });
    var registrationEntity = RegistrationEntity.of(currentUser, crashSessionEntity);
    return Registration.from(registrationEntityRepository.save(registrationEntity));
  }

  public void deleteRegistrationByRegistrationIdByCurrentUser(Long registrationId,
      UserEntity currentUser) {
    var registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
    registrationEntityRepository.delete(registrationEntity);
  }

  public CrashSessionRegistrationStatus getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
      Long sessionId, UserEntity currentUser) {
    var crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(sessionId);
    var ragistrationEntity = registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity);
    return new CrashSessionRegistrationStatus(
        sessionId,
        ragistrationEntity.isPresent(),
        ragistrationEntity.map(RegistrationEntity::getRegistrationId).orElse(null)
    );
  }
}
