package com.nnzz.nnzz.oauth;

import com.nnzz.nnzz.dto.KaKaoUserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com/")
public interface KakaoApiClient {
    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded")
    KaKaoUserInfoDTO getUserInfo(@RequestHeader("Authorization") String accessToken);
}
