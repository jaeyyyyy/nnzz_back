package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowCardDTO {
    private Integer cardId;
    private Integer userId;
    private String storeId;
    private Integer foodTypeId;
    private String name; // 가게 이름
    private String address; // 가게 주소
    private String category;
    private List<MenuDTO> menus; // 메뉴 정보
    private String date; // 날짜 정보
}
