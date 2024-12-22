package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "응답 반환용 객체")
public class ResponseDetail {
    @Schema(description = "응답 유형을 식별하는 참조", example = "about:blank")
    private String type;
    @Schema(description = "응답 유형에 대한 간단한 요약")
    private String title;
    @Schema(description = "응답의 응답 HTTP Status 코드")
    private int status;
    @Schema(description = "응답에 대한 간단한 설명")
    private String detail;
    @Schema(description = "응답이 발생한 URI")
    private String instance;
    @Schema(description = "응답 시간")
    private String timestamp;
    @Schema(description = "응답 메세지")
    private String message;
}
