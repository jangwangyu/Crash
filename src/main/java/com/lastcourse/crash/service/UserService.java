package com.lastcourse.crash.service;

import com.lastcourse.crash.exception.user.UserAlreadyExistsException;
import com.lastcourse.crash.exception.user.UserNotFoundException;
import com.lastcourse.crash.model.entity.UserEntity;
import com.lastcourse.crash.model.user.User;
import com.lastcourse.crash.model.user.UserAuthenticationResponse;
import com.lastcourse.crash.model.user.UserLoginRequestBody;
import com.lastcourse.crash.model.user.UserSignUpRequestBody;
import com.lastcourse.crash.repository.UserEntityCacheRepository;
import com.lastcourse.crash.repository.UserEntityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserEntityRepository userEntityRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserEntityCacheRepository userEntityCacheRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return getUserEntityByUsername(username);
  }

  public User signUp(@Valid UserSignUpRequestBody userSignUpRequestBody) {
    userEntityRepository.findByUsername(userSignUpRequestBody.username())
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException();
            }
        );

    var userEntity = userEntityRepository.save(
        UserEntity.of(
            userSignUpRequestBody.username(),
            passwordEncoder.encode(userSignUpRequestBody.password()),
            userSignUpRequestBody.name(),
            userSignUpRequestBody.email()
        ));
    return User.from(userEntity);
  }

  public UserAuthenticationResponse authenticate(@Valid UserLoginRequestBody userLoginRequestBody) {
    var userEntity = getUserEntityByUsername(userLoginRequestBody.username());

    if (passwordEncoder.matches(userLoginRequestBody.password(),
        userEntity.getPassword())) { // 인코딩된 패스워드를 matches로 userLoginRequestBody에 있는 password랑 userEntity에 있는 password 비교
      var accessToken = jwtService.generateAccessToken(userEntity); // 비교를 했을 때 일치하면 jwtToken 발급
      return new UserAuthenticationResponse(accessToken); // 발급받은 토큰을 클라이언트에 응답
    } else {
      throw new UserNotFoundException();
    }

  }

  private UserEntity getUserEntityByUsername(String username) {
//    var userEntityCache = userEntityCacheRepository.getUserEntityCache(username);
//    if (userEntityCache.isPresent()) {
//      return userEntityCache.get();
//    } else {
//      var userEntity = userEntityRepository
//          .findByUsername(username)
//          .orElseThrow(() -> new UsernameNotFoundException(username)); // 검색된 username이 없으면 예외 던짐
//
//      userEntityCacheRepository.setUserEntityRedisTemplate(userEntity); // 검색된 유저가 없으면 redis에 세팅을 함
//
//    return userEntity;
//    } if 문

    return userEntityCacheRepository
        .getUserEntityCache(username)
        .orElseGet( // null일때 수행
            () -> {
              var userEntity = userEntityRepository
                  .findByUsername(username)
                  .orElseThrow(
                      () -> new UsernameNotFoundException(username));
              userEntityCacheRepository.setUserEntityRedisTemplate(userEntity);
              return userEntity;
            }); // 함수형 (람다식)
  }
}
