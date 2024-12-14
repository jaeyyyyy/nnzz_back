package com.nnzz.nnzz.config.jwt;

import com.nnzz.nnzz.config.security.SecurityUser;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.repository.BlacklistTokenMapper;
import com.nnzz.nnzz.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final BlacklistTokenMapper blacklistTokenMapper;
    private final UserService userService;

    /**
     * 32자 이상
     * @param secretKey
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, @Qualifier("blacklistTokenMapper") BlacklistTokenMapper blacklistTokenMapper, UserService userService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.blacklistTokenMapper = blacklistTokenMapper;
        this.userService = userService;
    }


    // 유저 정보를 가지고 AccessToken, RefreshToken을 생성
    public JwtToken generateToken(Authentication authentication) {
        if(authentication == null) {
            throw new RuntimeException("Authentication 객체가 null입니다.");
        }

        String email = authentication.getName();
        if(email==null || email.isEmpty()) {
            throw new RuntimeException("Authentication 객체에서 유효한 이메일을 찾을 수 없습니다.");
        }

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();


        System.out.println("authentication:getName() : " + authentication.getName());
        // email에 해당하는 유저를 찾는다.
        UserDTO user = userService.getUserByEmail(authentication.getName());
        System.out.println("authentication:getName() : " + authentication.getName());
        System.out.println("authorities : " + authorities);
        System.out.println("user.getUserId() : " + user.getUserId());

        if(user == null) {
            throw new RuntimeException("User not found for email: " + authentication.getName());
        }

        /**
         * AccessToken 생성
         * 클레임으로 auth와 userId 사용
         */
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 24);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("userId", user.getUserId())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        /**
         * Refresh Token 생성
         * Refresh Token은 Calim에 아무것도 넣지 않는다.
         */
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") == null) {
            throw new AccountExpiredException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        int userId = claims.get("userId", Integer.class);

        // SecurityUser
        SecurityUser principal = new SecurityUser(userId, claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

        // UseInfoDetails 객체를 만들어서 Authentication return
        // UserInfoDetails principal = new UserInfoDetails(claims.getSubject(), "", authorities);

        // return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // 토큰을 파싱해서 만료시간을 함께 확인
    public boolean validateToken(String token) {
        if(blacklistTokenMapper.existsByToken(token)) {
            return false; // 블랙리스트에 존재하면 유효하지 않은 토큰
        }


        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
            // JWT 파싱
//            Jws<Claims> claimsJws = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//
//            // 만료 시간 확인
//            Date expiration = claimsJws.getBody().getExpiration();
//            return expiration.after(new Date()); // 현재 시간이 만료 시간보다 이전인지 확인
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken).getBody();
        } catch(SignatureException e) {
            throw new UnauthorizedException("서명이 조작된 토큰입니다.");
        } catch(MalformedJwtException e) {
            throw new UnauthorizedException("비정상적인 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("사용기간이 만료된 토큰입니다.");
        } catch(UnsupportedJwtException e) {
            throw new UnauthorizedException("지원하지않는 토큰입니다.");
        } catch(IllegalArgumentException e) {
            throw new UnauthorizedException("토큰이 잘못되었습니다.");
        }
    }
}
