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
@Schema(description = "카드 생성용 요청")
public class CardRequest {
    @Schema(description = "식당 아이디")
    private String storeId;
    @Schema(description = "식사 날짜 및 시간", example = "2024-12-23 저녁")
    private String date;
}
