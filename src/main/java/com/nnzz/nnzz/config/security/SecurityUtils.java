package com.nnzz.nnzz.config.security;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.dto.UserInfoDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class SecurityUtils {

    public static UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
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

        if(auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if(principal instanceof UserInfoDetails) {
            return (((UserInfoDetails)principal).getUserId());
        } else {
            return null;
        }
    }

    public static String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserInfoDetails) {
            return String.valueOf(((UserInfoDetails)principal).getUsername());
        } else {
            return principal.toString();
        }
    }
}
