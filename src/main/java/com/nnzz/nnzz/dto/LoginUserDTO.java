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
@Schema(description = "로그인한 유저의 정보")
public class LoginUserDTO {
    @Schema(description = "로그인한 유저의 아이디")
    private int id;
    @Schema(description = "로그인한 유저의 닉네임")
    private String nickname;
    @Schema(description = "로그인한 유저의 이메일")
    private String email;
    @Schema(description = "로그인한 유저의 프로필이미지")
    private String profileImage;
    @Schema(description = "로그인한 유저의 성별")
    private String gender;
    @Schema(description = "로그인한 유저의 나이")
    private String age;
}
