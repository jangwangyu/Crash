package com.lastcourse.crash.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.http.HttpStatus;

@JsonInclude(Include.NON_EMPTY)
public record ErrorResponse(HttpStatus status, String message) {

}
