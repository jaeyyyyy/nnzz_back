package com.nnzz.nnzz.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

// 잘못된 토큰 형식일 때, ProblemDetail 형식으로 예외 처리를 위한 엔트리 포인트
// Spring Security의 필터가 AuthenticationException 처리를 하기 때문에 따로 Security Config에 등록
@Component
@Getter
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // /error 가 아닌 실제 요청 URI를 가져오기 위해
        String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestURI == null) {
            requestURI = request.getRequestURI();
        }

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "올바른 형식의 토큰이 아닙니다.");
        pd.setInstance(URI.create(requestURI));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("message", e.getMessage());

        // json으로 반환하여 응답
        String problemDetail = objectMapper.writeValueAsString(pd);

        response.reset();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().append(problemDetail);
    }

}
