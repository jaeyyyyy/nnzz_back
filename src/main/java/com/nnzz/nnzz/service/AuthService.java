package com.nnzz.nnzz.service;

import com.nnzz.nnzz.config.jwt.JwtToken;
import com.nnzz.nnzz.config.jwt.JwtTokenProvider;
import com.nnzz.nnzz.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public JwtToken signIn(String email) {
        // 1. email을 기반으로 Authentication 객체 생성
        // 이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
        // 자동으로 CustomUserDetailsService에서 loadUserByUsername이 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        if(!authentication.isAuthenticated() || authentication == null) {
            throw new RuntimeException("Authentication failed for email: " + email);
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. accessToken 앞에 "Bearer "를 붙여서 반환
        String bearerAccessToken = "Bearer " + jwtToken.getAccessToken();

        //UserDTO loginUser = userService.getOptionalUserByEmail(email).orElse(null);
        //if (loginUser != null) {
        //    userService.updateLastLoginDate(loginUser.getUserId());
        //}

        // 5. 수정된 JwtToken 객체 반환
        return JwtToken.builder()
                .accessToken(bearerAccessToken)
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
