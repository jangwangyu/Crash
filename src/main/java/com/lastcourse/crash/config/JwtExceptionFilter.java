package com.lastcourse.crash.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastcourse.crash.model.error.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try{
      filterChain.doFilter(request, response);
    }catch (JwtException e) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 응답 JSON 형싱
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드 401
      response.setCharacterEncoding("UTF-8");
      var errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage()); // 오류 응답 객체 생성

      ObjectMapper objectMapper = new ObjectMapper(); // java 객체를 JSON 문자열로 변환
      String responseJson = objectMapper.writeValueAsString(errorResponse); // errorResponse 객체를 JSON 문자열(String) 으로 변환
      response.getWriter().write(responseJson); // 변환된 JSON 문자열을 클라이언트에게 전송
    }

  }
}
