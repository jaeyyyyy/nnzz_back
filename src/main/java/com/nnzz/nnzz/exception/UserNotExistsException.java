package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class UserNotExistsException extends RuntimeException {
    private Integer userId;

    public UserNotExistsException(Integer userId) {
        super("userId : " + userId + " 존재하지 않는 유저입니다.");
        this.userId = userId;
    }
}
