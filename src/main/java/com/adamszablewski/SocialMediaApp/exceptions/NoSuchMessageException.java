package com.adamszablewski.SocialMediaApp.exceptions;

public class NoSuchMessageException extends RuntimeException{
    public NoSuchMessageException(){}
    public NoSuchMessageException(String message){
        super(message);
    }
}
