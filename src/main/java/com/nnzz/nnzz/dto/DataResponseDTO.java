package com.nnzz.nnzz.dto;

import com.nnzz.nnzz.exception.Code;
import lombok.Getter;

@Getter
public class DataResponseDTO<T> extends ResponseDTO {
    private final T data;

    private DataResponseDTO(T data) {
        super(true, Code.OK.getCode(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponseDTO(String message, T data) {
        super(true, message, Code.OK.getCode());
        this.data = data;
    }

    public static <T> DataResponseDTO<T> of(T data) {
        return new DataResponseDTO<>(data);
    }

    public static <T> DataResponseDTO<T> of(String message, T data) {
        return new DataResponseDTO<>(message, data);
    }

    public static <T> DataResponseDTO<T> empty() {
        return new DataResponseDTO<>(null);
    }

    public static <T> DataResponseDTO<T> failure(T data) {
        return new DataResponseDTO<>(data);
    }

    public static <T> DataResponseDTO<T> failure(String message, T data) {
        return new DataResponseDTO<>(message, data);
    }
}
