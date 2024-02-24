package com.adamszablewski.SocialMediaApp.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomExceptionHandler {
    public static ResponseEntity<?> handleException(Throwable ex) {

        if (ex instanceof RuntimeException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }   else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
