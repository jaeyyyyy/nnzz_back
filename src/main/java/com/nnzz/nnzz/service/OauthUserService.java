package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.oauth.OauthUserDetails;
import com.nnzz.nnzz.oauth.KakaoUserDetails;
import com.nnzz.nnzz.oauth.OauthUserInfo;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OauthUserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : {}" + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OauthUserInfo oAuthUserInfo = null;

        // 추후 다른 소셜 서비스 로그인을 위해 구분
        if(provider.equals("kakao")) {
            System.out.println("kakao 로그인입니다.");
            oAuthUserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }

        String loginId = oAuthUserInfo.getProviderId();
        String email = oAuthUserInfo.getEmail();
        String name = oAuthUserInfo.getName();

        UserDTO findUser = userMapper.findUserByLoginId(email);
        UserDTO user;

        if(findUser == null) {
            user = UserDTO.builder()
                    .loginId(loginId)
                    .email(email)
                    .build();
            userMapper.createUser(user);
        } else {
            user = findUser;
        }

        return new OauthUserDetails(user, oAuth2User.getAttributes());
    }
}
