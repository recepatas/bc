package com.bc.authentication.service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User name and password is not valid");
    }
}