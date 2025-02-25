package com.lastcourse.crash.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lastcourse.crash.model.crashsession.CrashSession;
import com.lastcourse.crash.model.entity.CrashSessionEntity;
import com.lastcourse.crash.model.entity.UserEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfiguration {
  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()); // ZonedDateTime의 필드를 JSON으로 직렬화

  @Bean
  RedisConnectionFactory redisConnectionFactory(@Value("${redis.host") String redisHost,
      @Value("${redis.port}") int redisPort) {

    var config = new RedisStandaloneConfiguration();
    config.setHostName(redisHost);
    config.setPort(redisPort);
    return new LettuceConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, UserEntity> userEntityRedisTemplate(
      RedisConnectionFactory redisConnectionFactory
  ) {
    var template = new RedisTemplate<String, UserEntity>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer()); //StringRedisSerializer를 통해서 문자열로 변환하여 키를 세팅
    template.setValueSerializer(new Jackson2JsonRedisSerializer<UserEntity>(objectMapper, UserEntity.class)); // 전부 가져와서 쓸거지만 String 타입의 json문자열로 변환해서 사용
    return template;
  }

  @Bean
  public RedisTemplate<String, CrashSession> crashSessionRedisTemplate(
      RedisConnectionFactory redisConnectionFactory
  ) {
    var template = new RedisTemplate<String, CrashSession>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer()); //StringRedisSerializer를 통해서 문자열로 변환하여 키를 세팅
    template.setValueSerializer(new Jackson2JsonRedisSerializer<CrashSession>(objectMapper, CrashSession.class)); // 전부 가져와서 쓸거지만 String 타입의 json문자열로 변환해서 사용
    return template;
  }

  @Bean
  public RedisTemplate<String, List<CrashSession>> crashSessionsListRedisTemplate(
      RedisConnectionFactory redisConnectionFactory
  ) {
    var template = new RedisTemplate<String, List<CrashSession>>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer()); //StringRedisSerializer를 통해서 문자열로 변환하여 키를 세팅

    JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, CrashSession.class); // JackSon용도로 사용될 CrashSession.class를 리스트로 타입을 변경
    template.setValueSerializer(new Jackson2JsonRedisSerializer<List<CrashSession>>(objectMapper, type));
    return template;
  }
}
