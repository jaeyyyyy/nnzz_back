package com.nnzz.nnzz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.LocalDateTime;

@Getter
public class CustomProblemDetail extends ProblemDetail {
    // 추가할 필드
    private final String message; // 메세지
    private final LocalDateTime timestamp; // 시간

    public CustomProblemDetail(String type, String title, HttpStatus status, String detail, String instance, String message, LocalDateTime timestamp) {
        super();
        this.message = message; // message 필드 초기화
        this.timestamp = LocalDateTime.now();
        this.setType(URI.create(type));
        this.setTitle(title);
        this.setStatus(status);
        this.setDetail(detail);
        this.setInstance(URI.create(instance));
    }
}
