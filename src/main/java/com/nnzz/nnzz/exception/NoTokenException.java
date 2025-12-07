package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class NoTokenException extends RuntimeException {
    private final String message;

    public NoTokenException(String message) {
        this.message = message;
    }
}
