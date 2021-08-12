package com.example.codepile.error.user;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
