package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "식당을 찾기 위한 요청")
public class FindStoreRequest {
    @Schema(description = "위도")
    private double lat;
    @Schema(description = "경도")
    private double lng;
    @Schema(description = "날짜")
    private String day;
    @Schema(description = "카테고리 리스트")
    private List<Integer> categoryList;
}
