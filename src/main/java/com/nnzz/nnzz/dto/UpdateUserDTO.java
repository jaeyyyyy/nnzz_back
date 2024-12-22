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

    @Data
    public static class NicknameRequest {
        private String nickname;
    }

    @Data
    public static class ProfileImageRequest {
        private String profileImage;
    }

    @Data
    public static class AgeAndGenderRequest {
        private String ageRange;
        private String gender;
    }
}
