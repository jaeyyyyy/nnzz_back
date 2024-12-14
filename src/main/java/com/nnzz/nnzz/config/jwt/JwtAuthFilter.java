package com.nnzz.nnzz.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * GenericFilterBean 에서 OncePerRequestFilter 로 변경
 * GenericFilterBean의 경우 최초 1회 인증이 완료된 요청이라도, 해당 요청으로 여러 요청이 일어날 경우
 * 그 요청에 대해 모두 인증 처리가 들어감.
 * OncePerRequestFilter의 경우 최초 1회만 인증. 그 이후는 미인증 (한 번의 요청에 대해서는 한 번의 인증만 이루어진다)
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    // shouldNotFilter를 통하면 jwt 검증로직을 거치지 않음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/login") ||
                path.contains("/join") ||
                path.contains("/check") ||
                path.contains("/swagger") ||
                path.contains("/api-docs");
    }

    // doFilter에서 검증 실패 시, AuthenticationException 발생 Full authentication is required to access this resource
    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // preflight (OPTIONS) 요청인 경우에
        if(CorsUtils.isPreFlightRequest(req)) {
            // 필터 처리를 건너뛰고 다음 필터로 진행
            chain.doFilter(req, res);
            return;
        }

//        String path = req.getRequestURI();
//        if(path.startsWith("/api")) {
//            System.out.println("jwt필터 통과로직");
//            chain.doFilter(req, res);
//            return;
//        }

        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(req);

        // 2. validateToken 으로 토큰 유효성 검사
        if(token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(req, res);
    }


//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        // 1. Request Header에서 JWT 토큰 추출
//        String token = resolveToken((HttpServletRequest) req);
//
//        // 2. validateToken 으로 토큰 유효성 검사
//        if(token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//        chain.doFilter(req, res);
//    }

    /**
     * Request 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer xxx.. 이므로 Bearer 제외 순수 토큰만 가져온다.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더는 반드시 Bearer 토큰 형식으로 주어져야 합니다.");
        }

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
