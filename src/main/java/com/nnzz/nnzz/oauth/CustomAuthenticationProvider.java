package com.nnzz.nnzz.oauth;

import com.nnzz.nnzz.config.seed.Seed;
import com.nnzz.nnzz.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String token = authentication.getCredentials().toString();

        // UserDetails 사용
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Authentication 새 객체 생성
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
    }



//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String email = authentication.getName(); // 이메일
//        // String encryptedEmail = seed.encrypt(email); // 이메일 암호화
//
//        // UserDetailsService를 통해 사용자 정보 조회
//        UserDetails userInfoDetails = customUserDetailsService.loadUserByUsername(email); // 암호화된 이메일로 사용자 조회
//
//        if (userInfoDetails != null) {
//            // 인증 성공 시 Authentication 객체 생성
//            return new UsernamePasswordAuthenticationToken(userInfoDetails, null, userInfoDetails.getAuthorities());
//        } else {
//            throw new AuthenticationException("인증 실패: 사용자 정보가 잘못되었습니다.") {};
//        }
//    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
