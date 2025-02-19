package com.lastcourse.crash.config;

import com.lastcourse.crash.model.user.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfiguration {
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private JwtExceptionFilter jwtExceptionFilter;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() { // 다른 도메인에서 리소스에 접근할 수 있도록 허용하는 메소드
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000")); // CORS 요청을 허용할 출처
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE")); // HTTP 메소드
    configuration.setAllowedHeaders(List.of("*")); // 모든 값 허용
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 패턴에 따라 CORS 구성을 관리
    source.registerCorsConfiguration("/api/v1/**", configuration); // 특정 URL 패턴에 대한 CORS 구성
    return source;
  }


  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            (requests) -> requests
                .requestMatchers(HttpMethod.POST, "/api/*/users", "/api/*/users/authenticate") // POST로 받는 회원가입과 로그인으로 사용되는 url만
                .permitAll() // 인증 없이 허용
                .requestMatchers(HttpMethod.GET, "/api/*/session-speakers", "/api/*/session-speakers/*")
                .permitAll()
                .requestMatchers("/api/*/session-speakers", "/api/*/session-speakers/**") // HttpMethod가 없으면 전부 GET,POST,PATCH,DELETE 가능
                .hasAuthority(Role.ADMIN.name())
                .anyRequest()
                .authenticated())
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 서버가 세션을 저장하지 않도록 지정
        )
        .csrf(CsrfConfigurer::disable) // CSRF 토큰 X
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter보다 먼저 실행되도록 설정해서 사용자가 로그인할 때 JWT를 검증하고 인증 정보를 설정할 수 있도록 함
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())
        .httpBasic(HttpBasicConfigurer::disable); // HttpBasic 인증 비활성화
    return http.build();
  }
}
