package com.example.newsappwithauth.exception;

public class UserNotFound extends RuntimeException {
    UserNotFound(String message) {
        super(message);
    }

    public UserNotFound() {

    }
}
