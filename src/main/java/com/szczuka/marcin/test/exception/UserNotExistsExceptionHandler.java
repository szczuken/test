package com.szczuka.marcin.test.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserNotExistsExceptionHandler {

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity handleException(UserNotExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
