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
    private final UserService userService;

    public UserDTO getCurrentUser() {
        String userEmail = getUserEmail();
        UserDTO authUser = userService.getUserByEmail(userEmail);

        if(authUser == null) {
            throw new UnauthorizedException(userEmail); // getUserByEmail쓰는 메서드가 전부 안먹는 이유.. email이 전부 anonymousUser 로 나온다. 도대체왜? -> api들어가면 전부 통과시켜서 인증을 안하나싶어서 그걸 풀어줬다.
        } else { // 그니까 하튼..인증을 받아야하는 메서드들은 인증을 받아야된다. 걔네를 안받고 그냥 넘겨주면 인증을 못하니까 유저가 안뜬다.
            // AuthUser에서 가져온 userId
            return authUser;
        }
    }

    public int getUserId() {
        String userEmail = getUserEmail();
        UserDTO authUser = userService.getUserByEmail(userEmail);
        if (authUser == null) {
            throw new UnauthorizedException(userEmail); // getUserByEmail쓰는 메서드가 전부 안먹는 이유.. email이 전부 anonymousUser 로 나온다. 도대체왜? -> api들어가면 전부 통과시켜서 인증을 안하나싶어서 그걸 풀어줬다.
        } else { // 그니까 하튼..인증을 받아야하는 메서드들은 인증을 받아야된다. 걔네를 안받고 그냥 넘겨주면 인증을 못하니까 유저가 안뜬다.
            // AuthUser에서 가져온 userId
            return authUser.getUserId();
        }
    }

    public String getUserEmail() {
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
