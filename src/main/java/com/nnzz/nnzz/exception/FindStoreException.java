package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class FindStoreException extends IllegalArgumentException {

    public FindStoreException(String message) {
        super(message);
    }
}
