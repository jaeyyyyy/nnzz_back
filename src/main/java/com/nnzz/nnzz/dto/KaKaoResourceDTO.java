package com.nnzz.nnzz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KaKaoResourceDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private Object kakaoAccount;
}
