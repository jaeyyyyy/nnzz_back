package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends IllegalArgumentException {
    private final String field;

    public InvalidValueException(String field) {
        super("field : " + field + " 글자수가 초과/미만이거나 유효한 형태의 값이 아닙니다.");
        this.field = field;
    }
}
