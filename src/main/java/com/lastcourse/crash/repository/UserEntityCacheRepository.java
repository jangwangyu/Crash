package com.lastcourse.crash.repository;

import com.lastcourse.crash.model.entity.UserEntity;
import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserEntityCacheRepository {
  @Autowired
  private RedisTemplate<String, UserEntity> userEntityRedisTemplate;

  public void setUserEntityRedisTemplate(UserEntity userEntity) {
    var redisKey = getRedisKey(userEntity.getUsername());
    userEntityRedisTemplate.opsForValue().set(redisKey, userEntity, Duration.ofSeconds(30));
  }

  public Optional<UserEntity> getUserEntityCache(String username){
    var redisKey = getRedisKey(username);
    var userEntity = userEntityRedisTemplate.opsForValue().get(redisKey);
    return Optional.ofNullable(userEntity);
  }

  private String getRedisKey(String username){
    return "user:" + username;
  }
}
