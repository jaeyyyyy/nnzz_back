package com.nnzz.nnzz.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
    OK(HttpStatus.OK, "COMMON200", "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST400", "클라이언트 오류"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION400","Validation 오류"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND404", "NOT FOUND"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR500", "내부 서버 오류"),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATA_ACCESS_ERROR500", "데이터 접근 불가"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED401", "UNAUTHORIZED");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new NullPointerException("httpStatus is null");
        }
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return Code.BAD_REQUEST;
                    } else if(httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
