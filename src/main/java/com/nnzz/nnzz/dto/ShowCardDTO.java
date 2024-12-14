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
    private int cardId;
    private int userId;
    // List<FoodTypeDTO> foodTypes; // 카테고리 관련

    // 카테고리
    private int foodTypeId;
    private String category;
    private String description;

    // List<CardStoreDTO> stores; // 가게 관련

    // 가게
    private String storeId;
    private String name;
    private String address;
    private List<MenuDTO> menus; // 메뉴 정보

    private LocalDate cardDate; // 날짜 정보
    private String mealtime; // 점심, 저녁
    private Timestamp createdAt; // 생성한 날짜
}
