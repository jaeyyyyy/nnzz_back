package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends IllegalStateException {
    private final String email;

    public UserAlreadyExistsException(String email) {
        super("email : " + email + " 은 이미 등록되어 있습니다.");
        this.email = email;
    }

}
