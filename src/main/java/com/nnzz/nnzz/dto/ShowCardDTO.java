package com.nnzz.nnzz.dto;

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
public class ShowCardDTO {
    private Integer cardId;
    private Integer userId;
    List<FoodTypeDTO> foodTypes; // 카테고리 관련
    List<CardStoreDTO> stores; // 가게 관련
    private LocalDate cardDate; // 날짜 정보
    private String mealtime; // 점심, 저녁
    private Timestamp createdAt; // 생성한 날짜
}
