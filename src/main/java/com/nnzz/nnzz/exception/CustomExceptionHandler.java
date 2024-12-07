package com.nnzz.nnzz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class CustomExceptionHandler {

    // ProblemDetail 만드는 메서드
    // setTitle은 HttpStatus.에서 자동으로 갖고 옵니다
    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String message) {
        final ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("message", message);
        return pd;
    }

    // 유저
    // 존재하지 않는 유저
    @ExceptionHandler(UserNotExistsException.class)
    public ProblemDetail handleUserNotExistsException(UserNotExistsException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", ex.getMessage());
//        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
//                HttpStatus.NOT_FOUND, ex.getMessage());
//        pd.setTitle("존재하지 않는 유저입니다.");
//        pd.setProperty("timestamp", LocalDateTime.now());
//        pd.setProperty("message", ex.getMessage());
//        return pd;
    }

    // 이메일 중복(이미 존재하는 유저)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "이미 회원가입 한 유저입니다.", ex.getMessage());
    }

    // 인증되지 않은 유저
    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex) {
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다.", ex.getMessage());
    }

    // 회원가입 시 오류
    // 닉네임 중복
    @ExceptionHandler(NicknameDuplicateException.class)
    public ProblemDetail handleNicknameException(NicknameDuplicateException ex) {
        return createProblemDetail(HttpStatus.CONFLICT, "닉네임이 중복됩니다.", ex.getMessage());
    }

    // 회원 수정 시 오류
    // 닉네임 업데이트 횟수 초과
    @ExceptionHandler(NicknameUpdateException.class)
    public ProblemDetail handleNicknameUpdateException(NicknameUpdateException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "닉네임은 30일에 한 번만 수정 가능합니다.", ex.getMessage());
    }

    // 두 값이 일치하지 않음
    @ExceptionHandler(InconsistentException.class)
    public ProblemDetail handleInconsistentException(InconsistentException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "아이디가 일치하지 않습니다.", ex.getMessage());
    }

    // 글자수 초과 미만 등 올바른 형태가 아닌 값
    @ExceptionHandler(InvalidValueException.class)
    public ProblemDetail handleInvalidValueException(InvalidValueException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "유효한 값이 아닙니다.", ex.getMessage());
    }




    // 오픈되지 않은 지역
    @ExceptionHandler(InvalidLocationException.class)
    public ProblemDetail handleInvalidLocationException(InvalidLocationException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "오픈되지 않은 지역입니다.", ex.getMessage());
    }

    // 날짜 형식이 맞지 않음
    @ExceptionHandler(DateTimeParseException.class)
    public ProblemDetail handleDateTimeParseException(DateTimeParseException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜 형식입니다.", ex.getMessage());
    }



}
