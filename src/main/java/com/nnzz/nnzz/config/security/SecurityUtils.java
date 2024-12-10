package com.nnzz.nnzz.config.security;

import com.nnzz.nnzz.config.seed.Seed;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.dto.UserInfoDetails;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class SecurityUtils {
    private static Seed seed;
    private static UserService userService;

    public static UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new UnauthorizedException("인증 세션이 존재하지 않습니다."); // 세션 없음
        }
        if (!auth.isAuthenticated()) {
            throw new UnauthorizedException("로그인 세션이 만료되었습니다."); // 세션 만료
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserInfoDetails) {
            return ((UserInfoDetails)principal).getUserInfo();
        } else {
            return null;
        }
    }

    public static Integer getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new UnauthorizedException("인증 세션이 존재하지 않습니다."); // 세션 없음
        }
        if (!auth.isAuthenticated()) {
            throw new UnauthorizedException("로그인 세션이 만료되었습니다."); // 세션 만료
        }

        Object principal = auth.getPrincipal();

        if(principal instanceof UserInfoDetails) {
            return (((UserInfoDetails)principal).getUserId());
        } else {
            return null;
        }
    }

    public static String getUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new UnauthorizedException("인증 세션이 존재하지 않습니다."); // 세션 없음
        }
        if (!auth.isAuthenticated()) {
            throw new UnauthorizedException("로그인 세션이 만료되었습니다."); // 세션 만료
        }

        return auth.getName();
        // return seed.decrypt(auth.getName());
//        Object principal = auth.getPrincipal();
//        if(principal instanceof UserInfoDetails) {
//            return String.valueOf(((UserInfoDetails)principal).getUsername());
//        } else {
//            return principal.toString();
//        }
    }
}
