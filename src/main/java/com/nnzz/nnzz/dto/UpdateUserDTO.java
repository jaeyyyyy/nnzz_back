package com.nnzz.nnzz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "수정할 유저 정보")
public class UpdateUserDTO {
    private String nickname;
    private String profileImage;
    private String gender;
    private String ageRange;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NicknameRequest {
        @Schema(description = "수정할 유저 닉네임")
        private String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileImageRequest {
        @Schema(description = "수정할 유저 프로필 이미지")
        private String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgeAndGenderRequest {
        @Schema(description = "수정할 유저 나이대")
        private String ageRange;
        @Schema(description = "수정할 유저 성별")
        private String gender;
    }
}
