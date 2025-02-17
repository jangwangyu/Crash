package com.lastcourse.crash.config;

import com.lastcourse.crash.service.JwtService;
import com.lastcourse.crash.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component// custom security chain filter
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // JWT 인증로직
    String BEARER_PREFIX = "Bearer ";
    var authorization = request.getHeader(HttpHeaders.AUTHORIZATION); // requst 헤더에 있는 AURHORIZATION 추출
    var securityContext = SecurityContextHolder.getContext();

    if (!ObjectUtils.isEmpty(authorization) && // authorization 값이 비어있지 않으면서 동시에
        authorization.startsWith(BEARER_PREFIX) && // BEARER_PREFIX로 시작하는 경우에만
        securityContext.getAuthentication() == null){ // 인증 정보가 null일 경우에 한해서 인증 로직 수행

      var accessToken = authorization.substring(BEARER_PREFIX.length()); //authorization의 BEARER_PREFIX만으로 토큰 저장
      var username = jwtService.getUsername(accessToken); // accessToken으로부터 username을 가져옴
      var userDetails = userService.loadUserByUsername(username); // userDetails 정보를 조회해서 가져옴

      var authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 해당 토큰에다 setDetails를 통해 현재 처리되고 있는 API의 정보도 포함
      // 생성 완료된 인증 정보 로직

      securityContext.setAuthentication(authenticationToken); // securityContext.setAuthentication 통해서 세팅
      SecurityContextHolder.setContext(securityContext); // securityContext를 SecurityContextHolder.setContext 통해 세팅
    }

    filterChain.doFilter(request, response); // 커스텀 필터를 작성할때 기본 틀
  }

}
