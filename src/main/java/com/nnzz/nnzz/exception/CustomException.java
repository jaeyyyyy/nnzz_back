package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String path;
    private final String message;

    public CustomException(UserError userError, String path, String email) {
        super(userError.getMessage(email));
        this.path = path;
        this.message = userError.getMessage(email);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
