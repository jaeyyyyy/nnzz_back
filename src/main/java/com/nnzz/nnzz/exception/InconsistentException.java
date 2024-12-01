package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class InconsistentException extends RuntimeException {
    private final Integer userId;
    private final Integer bodyId;

    public InconsistentException(final Integer userId, final Integer bodyId) {
        super("userId 값 : " + userId + "와 " + bodyId + " 가 일치하지 않습니다.");
        this.userId = userId;
        this.bodyId = bodyId;
    }
}
