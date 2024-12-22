package com.nnzz.nnzz.dto;

import com.nnzz.nnzz.config.jwt.JwtToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 정보와 토큰 전달을 위한 response")
public class UserResponse {
    @Schema(description = "로그인한 유저의 정보")
    private LoginUserDTO user;
    @Schema(description = "로그인한 유저의 토큰 정보")
    private JwtToken token;
}
