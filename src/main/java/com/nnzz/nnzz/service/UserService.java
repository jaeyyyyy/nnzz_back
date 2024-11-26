package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.oauth.KakaoClient;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final KakaoClient kakaoClient;
    private final AuthService authService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

//    /**
//     * 카카오 로그인 정보를 가져옴
//     * @param code
//     * @return
//     */
//    @Transactional
//    public DataResponseDTO<?> getKakaoInfo(String code) {
//        KakaoTokenDTO token = kakaoClient.generateToken("authorization_code", clientId, redirectUri, clientSecret, code);
//        KaKaoUserInfoDTO userInfo = authService.getKakaoUserInfo("Bearer " + token.getAccessToken());
//        String email = userInfo.getEmail();
//        UserDTO user = userMapper.findUserByEmail(email); // 이메일로 유저찾기
//
//        if(user == null) {
//            return DataResponseDTO.failure("로그인 실패, 회원가입을 진행합니다.", registerUser(user));
//        } else {
//            return DataResponseDTO.of()
//        }
//    }


    // 유저 찾기
    public UserDTO getUser(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    // 회원정보 수정
    public void updateUser(UserDTO user){
        userMapper.updateUser(user);
    }

    // 회원가입 처리
    public void registerUser(UserDTO user) {
        userMapper.createUser(user);
    }

    // 이메일 중복 찾기
    public boolean checkEmailExists(String email) {
        return userMapper.existsUserByEmail(email);
    }

    // 닉네임 중복 찾기
    public boolean checkNicknameExists(String nickname) {
        return userMapper.existsUserByNickname(nickname);
    }

    // 실제 있는 userId인지 확인
    public boolean checkUserIdExists(int userId) {
        return userMapper.existsUserByUserId(userId);
    }
}
