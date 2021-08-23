package com.example.codepile.error.pile;

import com.example.codepile.error.base.BaseException;
import org.springframework.http.HttpStatus;

public class PileNotFoundException extends BaseException {
    public PileNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
