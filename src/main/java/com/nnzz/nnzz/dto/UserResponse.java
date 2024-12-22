package com.nnzz.nnzz.dto;

import com.nnzz.nnzz.config.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private JwtToken token;
    private LoginUserDTO user;
}
