package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }
}
