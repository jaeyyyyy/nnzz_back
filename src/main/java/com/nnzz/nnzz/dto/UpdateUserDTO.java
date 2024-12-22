package com.nnzz.nnzz.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
        private String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileImageRequest {
        private String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgeAndGenderRequest {
        private String ageRange;
        private String gender;
    }
}
