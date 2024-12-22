package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetail {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String timestamp;
    private String message;
}
