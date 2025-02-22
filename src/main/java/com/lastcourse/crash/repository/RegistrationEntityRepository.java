package com.lastcourse.crash.repository;

import com.lastcourse.crash.model.entity.CrashSessionEntity;
import com.lastcourse.crash.model.entity.RegistrationEntity;
import com.lastcourse.crash.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationEntityRepository extends JpaRepository<RegistrationEntity,Long> {
  List<RegistrationEntity> findByUser(UserEntity user); // 사용자 기준으로 모든 레지스트레이션을 가져올 수 있게끔

  Optional<RegistrationEntity> findByRegistrationIdAndUser(Long registrationId, UserEntity user); // registrationId가 user에 세팅이 되어있는지 확인

  Optional<RegistrationEntity> findByUserAndSession(UserEntity user, CrashSessionEntity session); // 사용자 정보와 세션 정보를 활용해서 레지스트레이션을 새롭게 만들어 줄 텐데 기존에 신청한 적이 있다면 중복 생성 못하게끔 생성하기 전에 사전조회

}
