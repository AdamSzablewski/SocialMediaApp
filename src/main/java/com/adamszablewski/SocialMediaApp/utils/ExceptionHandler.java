package com.adamszablewski.SocialMediaApp.utils;

import com.adamszablewski.SocialMediaApp.exceptions.IncompleteDataException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {
    public ResponseEntity<String> handleException(Throwable throwable){
        if(throwable instanceof IncompleteDataException || throwable instanceof NoSuchUserException || throwable instanceof UserAlreadyExistException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
