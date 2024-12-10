package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime lastNicknameChangeDate;
}
