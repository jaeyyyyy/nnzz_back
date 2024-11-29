package com.nnzz.nnzz.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserError {
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", "Bad Request", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 유저입니다.", "Bad Request", HttpStatus.BAD_REQUEST),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임입니다.", "Conflict", HttpStatus.CONFLICT),
    NOT_CORRECT_USERID("경로의 userId와 본문에 있는 userId가 일치하지 않습니다.", "Bad Request", HttpStatus.BAD_REQUEST);


    private final String detail;
    private final String title;
    private final HttpStatus status;

    public String getMessage(String email) {
        return String.format(email);
    }
}
