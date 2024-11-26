package com.nnzz.nnzz.oauth;

import com.nnzz.nnzz.dto.KakaoTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient", url = "https://kauth.kakao.com/")
public interface KakaoClient {
    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    KakaoTokenDTO generateToken(@RequestParam("grant_type") String grantType,
                                @RequestParam("client_id") String clientId,
                                @RequestParam("redirect_uri") String redirectUri,
                                @RequestParam("client_secret") String clientSecret,
                                @RequestParam("code") String code);
}
