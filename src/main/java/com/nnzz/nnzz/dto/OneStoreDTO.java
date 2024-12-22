package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "특정 식당 한 곳의 정보")
public class OneStoreDTO {
    @Schema(description = "식당 id")
    private String storeId;
    @Schema(description = "식당 이름")
    private String name;
    @Schema(description = "식당의 위도")
    private double lat;
    @Schema(description = "식당의 경도")
    private double lng;
    @Schema(description = "식당의 주소")
    private String address;
    @Schema(description = "식당과 사용자와의 거리")
    private int distance;
    @Schema(description = "식당의 냠냠쩝쩝 기준 카테고리")
    private String category;
    @Schema(description = "식당의 카테고리 id")
    private int categoryId;
    @Schema(description = "식당의 카테고리 설명")
    private String description;
    @Schema(description = "식당의 방송 출연 정보")
    private List<BroadcastDTO> broadcasts;
    @Schema(description = "식당의 메뉴 정보")
    private List<MenuDTO> menus;
}
