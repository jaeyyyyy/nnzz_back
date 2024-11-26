package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.KaKaoUserInfoDTO;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.oauth.KakaoApiClient;
import com.nnzz.nnzz.oauth.KakaoUtil;
import com.nnzz.nnzz.repository.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
     private final KakaoApiClient kakaoApiClient;


    /**
     * 카카오 로그인 정보를 가져옴
     * @param accessToken 카카오 액세스 토큰
     * @return 카카오 id와 email을 담은 응답 DTO
     */
    public KaKaoUserInfoDTO getKakaoUserInfo(String accessToken) {
        KaKaoUserInfoDTO userInfo = kakaoApiClient.getUserInfo(accessToken);

        return userInfo;
    }
}
