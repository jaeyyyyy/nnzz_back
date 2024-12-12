package com.nnzz.nnzz.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistToken {
    private int id;
    private String token;
    private LocalDateTime expiry;


    public BlacklistToken(String token, LocalDateTime expiry) {
        this.token = token;
        this.expiry = expiry;
    }

}
