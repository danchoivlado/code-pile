package com.example.codepile.error.user;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException  extends BaseException {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT.value() );
    }
}
