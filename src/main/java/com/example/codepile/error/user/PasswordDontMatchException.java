package com.example.codepile.error.user;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordDontMatchException extends BaseException {
    public PasswordDontMatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
