package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    private String storeId; // 가게 id
    private String name; // 가게 이름
    private double lat; // 위도
    private double lng; // 경도
    private String address; // 가게 주소
    private Integer distance; // 사용자와의 거리
    private String category; // 냠냠쩝쩝 카테고리
    private List<BroadcastDTO> broadcasts; // 방송 정보
    private List<MenuDTO> menus; // 메뉴 정보
}
