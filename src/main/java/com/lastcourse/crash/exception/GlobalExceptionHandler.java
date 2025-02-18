package com.lastcourse.crash.exception;

import com.lastcourse.crash.model.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ClientErrorException.class) // clientException이 발생했을때, 예외를 핸들링 할 ExceptionHandler 작성
  public ResponseEntity<ErrorResponse> handleClientErrorException(ClientErrorException e){
    return new ResponseEntity<>(
        new ErrorResponse(e.getStatus(), e.getMessage()),
        e.getStatus()
    );
  } // Client 에러 (4XX)


  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleClientErrorException(RuntimeException e){
    return ResponseEntity.internalServerError().build();
  } // server 에러 (5XX)

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleClientErrorException(Exception e){
    return ResponseEntity.internalServerError().build();
  }// server 에러 (5XX)


}
