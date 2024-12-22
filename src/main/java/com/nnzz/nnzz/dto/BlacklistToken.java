package com.nnzz.nnzz.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "토큰 처리용 블랙리스트")
public class BlacklistToken {
    @Schema(description = "블랙리스트에 저장된 토큰 id")
    private int id;
    @Schema(description = "토큰 값")
    private String token;
    @Schema(description = "만료일자")
    private LocalDateTime expiry;


    public BlacklistToken(String token, LocalDateTime expiry) {
        this.token = token;
        this.expiry = expiry;
    }

}
