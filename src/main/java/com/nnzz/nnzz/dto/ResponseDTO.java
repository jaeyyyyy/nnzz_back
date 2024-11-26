package com.nnzz.nnzz.dto;

import com.nnzz.nnzz.exception.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDTO {
    private final Boolean success;
    private final String code;
    private final String message;

    public static ResponseDTO of(Boolean success, Code code) {
        return new ResponseDTO(success, code.getCode(), code.getMessage());
    }

    public static ResponseDTO of(Boolean success, Code errorcode, Exception e) {
        return new ResponseDTO(success, errorcode.getCode(), e.getMessage());
    }

    public static ResponseDTO of(Boolean success, Code errorcode, String message) {
        return new ResponseDTO(success, errorcode.getCode(), errorcode.getMessage(message));
    }

    public static ResponseDTO failure(Code errorcode, String message) {
        return new ResponseDTO(false, errorcode.getCode(), message);
    }

    public static ResponseDTO failure(Code errorcode, Exception e) {
        return new ResponseDTO(false, errorcode.getCode(), e.getMessage());
    }
}
