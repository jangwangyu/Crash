package com.lastcourse.crash.service;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
  private final SecretKey key;

  public JwtService(@Value("${jwt.secret-key}") String key) {
    try {
      this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    } catch (Exception e) {
      logger.error("Failed to initialize JwtService: {}", e.getMessage());
      throw e; // 예외를 다시 던져서 빈 생성이 실패하도록 함
    }
  }

  public String generateAccessToken(UserDetails userDetails) { // 사용자 인증을 통해 인증이 완료된 유저의 username을 subject로 대입해서 토큰을 만들고 generateAccessToken에 제공하는 함수
    return generateToken(userDetails.getUsername());
  }

  public String getUsername(String accessToken){ // 생성된 accessToken으로부터 다시 subject(username)를 추출해서 전달해주는 함수
    return  getSubject(accessToken);
  }


  private String generateToken(String subject) { // jwt 토큰을 생성해주는 generateToken 함수
    var now = new Date(); // 현재 시각
    var exp = new Date(now.getTime() + (1000 * 60 * 60 * 3));// 현재 시각을 기준으로 3시간 이후를 만료 시점 세팅
    return Jwts.builder().subject(subject).signWith(key) // 서브젝트를 담아 키로 사인을 함
        .issuedAt(now) // 토큰 발행 시점은 현재
        .expiration(exp) // 만료 시점은 3시간 뒤
        .compact();
  } // subject에 인증된 사용자의 username을 넣어줄 예정

  private String getSubject(String token) { // subject가 담겨있던 token을 전달받아서
    try{
      return Jwts.parser()
          .verifyWith(key) // 해당 모듈에서 제공하는 parser를 통해 키 값으로 먼저 검증을 한 후,
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject(); // 해당 token의 payload에 저장되어 있던 subject 값을 추출해내는 로직
    }catch (JwtException e){
      logger.error("JwtException", e.getMessage());
      throw e;
    } // jwt의 시그니처 검증, 유효기간도 검증함 검증에 실패하면 exception 발생
  }
}
