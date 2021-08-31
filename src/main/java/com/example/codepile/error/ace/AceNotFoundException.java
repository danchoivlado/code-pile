package com.example.codepile.error.ace;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;

public class AceNotFoundException extends BaseException {
    public AceNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}