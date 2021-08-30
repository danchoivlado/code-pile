package com.example.codepile.error.authority;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;

public class AuthorityNotFoundException extends BaseException {
    public AuthorityNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}