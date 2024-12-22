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
@Schema(description = "식당 카테고리 dto")
public class CategoryDTO {
    @Schema(description = "식당 카테고리")
    private String category;
    @Schema(description = "식당 카테고리 아이디")
    private int categoryId;
    @Schema(description = "해당 카테고리 식당 중 사용자와 가장 가까이 있는 가게의 거리")
    private int distance;
    @Schema(description = "카테고리별 대표 메뉴")
    private String represent;
}
