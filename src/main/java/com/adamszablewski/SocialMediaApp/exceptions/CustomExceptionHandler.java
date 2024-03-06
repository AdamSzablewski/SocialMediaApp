package com.adamszablewski.SocialMediaApp.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomExceptionHandler {
    public static ResponseEntity<?> handleException(Throwable ex) {
        ex.printStackTrace();

        if (ex instanceof InvalidCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else if (ex instanceof NoFriendRequestException ||
                ex instanceof NoSuchPostException ||
                ex instanceof NoSuchConversationFoundException ||
                ex instanceof NoSuchProfileException ||
                ex instanceof NoSuchUpvoteException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else if (ex instanceof UserAlreadyExistException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
