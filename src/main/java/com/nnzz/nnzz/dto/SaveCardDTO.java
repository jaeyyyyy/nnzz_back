package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "저장된 식당 카드")
public class SaveCardDTO {
    @Schema(description = "카드 id")
    private int cardId;
    @Schema(description = "카드를 저장한 유저의 id")
    private int userId;
    @Schema(description = "카드에 저장할 식당의 id")
    private String storeId;
    @Schema(description = "식당의 카테고리 id")
    private int foodTypeId;
    @Schema(description = "카드에 저장될 식사할 날짜", example = "2024-12-23")
    private LocalDate cardDate;
    @Schema(description = "카드에 저장될 식사할 시간", example = "저녁")
    private String mealtime;
}
