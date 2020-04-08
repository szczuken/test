package com.szczuka.marcin.test.exception;

public class SubscriptionAlreadyExistsException extends Exception {
    public SubscriptionAlreadyExistsException() {
        super("You already following this user!");
    }
}
