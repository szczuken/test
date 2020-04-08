package com.szczuka.marcin.test.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubscriptionAlreadyExistsHandler {

    @ExceptionHandler(SubscriptionAlreadyExistsException.class)
    public ResponseEntity handleException(SubscriptionAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
