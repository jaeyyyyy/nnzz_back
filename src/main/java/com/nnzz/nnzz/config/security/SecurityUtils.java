package com.nnzz.nnzz.config.security;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    public static int getUserId() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            // Authentication 객체에 유효한 principal 객체가 없음
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
        Object principal = auth.getPrincipal();
        if(principal instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) principal;
            return user.getUserId();
        } else {
            // Authentication 객체에 anonymousUser 객체가 있음
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
    }

    public static String getUserEmail() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            // Authentication 객체에 유효한 principal 객체가 없음
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
        if (auth.getName().equals("anonymousUser")) {
            // Authentication 객체에 anonymousUser 객체가 있음
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
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
