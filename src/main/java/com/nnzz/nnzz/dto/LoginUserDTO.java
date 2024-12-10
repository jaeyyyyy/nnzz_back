package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {
    private int id;
    private String nickname;
    private String email;
    private String profileImage;
    private String gender;
    private String age;
}
