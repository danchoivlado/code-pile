package com.example.codepile.error.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException{
    private int code;

    public BaseException(String message, int code) {
        super(message);
        this.setCode(code);
    }
}
