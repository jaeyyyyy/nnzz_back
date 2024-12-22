package com.nnzz.nnzz.exception;

public class NullException extends IllegalArgumentException {
    public NullException() {
        super("수정하려는 값이 null입니다.");
    }
}
