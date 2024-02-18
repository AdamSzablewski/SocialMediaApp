package com.adamszablewski.SocialMediaApp.exceptions;

public class InvalidCredentialsException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "The email or password is incorrect";

    public InvalidCredentialsException(String message) {
        super(message);
    }
    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }
}
