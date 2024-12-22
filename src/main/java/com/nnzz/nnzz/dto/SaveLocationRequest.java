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
@Schema(description = "위치 저장을 위한 요청")
public class SaveLocationRequest {
    @Schema(description = "위도")
    private double lat;
    @Schema(description = "경도")
    private double lng;
    @Schema(description = "주소")
    private String address;
}
