package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String loginId;
    private String email;
    private String nickname;
    private String profileImage;
    private String gender;
    private String ageRange;
    private LocalDateTime joinDate; // 회원가입일자 추가
    private LocalDateTime lastNicknameChangeDate;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입에 필요한 요청")
    public static class JoinRequest {
        @Schema(description = "유저 이메일")
        private String email;
        @Schema(description = "유저 닉네임")
        private String nickname;
        @Schema(description = "유저 프로필 이미지")
        private String profileImage;
        @Schema(description = "유저 성별")
        private String gender;
        @Schema(description = "유저 나이대")
        private String ageRange;
    }
}
