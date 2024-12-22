package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카드에서 보여줄 정보")
public class ShowCardDTO {
    @Schema(description = "카드의 id")
    private int cardId;
    @Schema(description = "카드를 생성한 유저의 id")
    private int userId;
    @Schema(description = "카드에 저장된 식당의 카테고리 id")
    private int foodTypeId;
    @Schema(description = "카드에 저장된 식당의 카테고리")
    private String category;
    @Schema(description = "카드에 저장된 식당의 카테고리 소개문구")
    private String description;
    @Schema(description = "카드에 저장된 식당의 id")
    private String storeId;
    @Schema(description = "카드에 저장된 식당의 이름")
    private String name;
    @Schema(description = "카드에 저장된 식당의 주소")
    private String address;
    @Schema(description = "카드에 저장된 식당의 메뉴 정보")
    private List<MenuDTO> menus;
    @Schema(description = "카드에 저장된 식사할 날짜", example = "2024-12-23")
    private LocalDate cardDate;
    @Schema(description = "카드에 저장된 식사할 시간", example = "저녁")
    private String mealtime;
    @Schema(description = "카드를 생성한 날짜")
    private Timestamp createdAt;
}
