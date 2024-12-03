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
public class CardStoreDTO {
    private String storeId;
    private String name;
    private String address;
    private List<MenuDTO> menus; // 메뉴 정보
}
