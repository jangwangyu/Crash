package com.lastcourse.crash.exception;

import com.lastcourse.crash.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException
      (MethodArgumentNotValidException e){
    var errorMessage =
        e.getFieldErrors().stream() // 모든 에러들을 필드 단위로 가져오고
            .map(fieldError -> (fieldError.getField() + ": " + fieldError.getDefaultMessage())) // 필드 이름이랑 실제로 발생한 에러 메세지를 하나 정도만 추출해서 에러 메세지로 만듦
            .toList()
            .toString();
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException
      (HttpMessageNotReadableException e){

    return new ResponseEntity<>(
        new ErrorResponse(HttpStatus.BAD_REQUEST, "Required request body is missing"), HttpStatus.BAD_REQUEST);

  }


  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleClientErrorException(RuntimeException e){
    return ResponseEntity.internalServerError().build();
  } // server 에러 (5XX)

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleClientErrorException(Exception e){
    return ResponseEntity.internalServerError().build();
  }// server 에러 (5XX)


}
