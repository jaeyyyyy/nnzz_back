package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메뉴 정보")
public class MenuDTO {
    @Schema(description = "메뉴 이름")
    private String menuName;
    @Schema(description = "메뉴 설명")
    private String description;
    @Schema(description = "메뉴 가격")
    private String price;
}
