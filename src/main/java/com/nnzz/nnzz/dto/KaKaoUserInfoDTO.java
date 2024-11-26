package com.nnzz.nnzz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoUserInfoDTO {
    @JsonProperty("id")
    private String loginId; // 카카오 고유 아이디
    @JsonProperty("email")
    private String email; // 이메일
}
