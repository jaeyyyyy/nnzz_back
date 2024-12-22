package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 위치 DTO")
public class UserLocationDTO {
    @Schema(description = "위치 id")
    private int locationId;
    @Schema(description = "유저 id")
    private int userId;
    @Schema(description = "위치의 위도")
    private double lat;
    @Schema(description = "위치의 경도")
    private double lng;
    @Schema(description = "위치의 주소")
    private String address;
    @Schema(description = "위치를 저장한 날짜")
    private LocalDateTime createdAt;
}
