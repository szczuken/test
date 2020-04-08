package com.szczuka.marcin.test.exception;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(Long id) {
        super(String.format("User with id %d does not exist.", id));
    }
}
