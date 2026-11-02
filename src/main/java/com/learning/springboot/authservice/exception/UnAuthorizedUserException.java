package com.learning.springboot.authservice.exception;

public class UnAuthorizedUserException extends RuntimeException {
    public UnAuthorizedUserException(String message) {
        super(message);
    }
}
