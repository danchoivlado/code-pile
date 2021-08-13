package com.example.codepile.error.user;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;


public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
