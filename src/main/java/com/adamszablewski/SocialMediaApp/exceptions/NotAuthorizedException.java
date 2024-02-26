package com.adamszablewski.SocialMediaApp.exceptions;

public class NotAuthorizedException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "User not authorized";

    public NotAuthorizedException(String message) {
        super(message);
    }
    public NotAuthorizedException() {
        super(DEFAULT_MESSAGE);
    }
}
