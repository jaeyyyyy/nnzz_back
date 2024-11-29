package com.nnzz.nnzz.oauth;

import com.nnzz.nnzz.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AuthUserDetails implements UserDetails {

    private final UserDTO user;
    private final Map<String, Object> attributes;

    public AuthUserDetails(UserDTO user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    public String getUserNickname() {
        return user.getNickname();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getProfileImage() {
        return user.getProfileImage();
    }

    public String getGender() {
        return user.getGender();
    }

    public String getAgeRange() {
        return user.getAgeRange();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        return collection;
    }

    // 계정이 만료되지 않았는지 (true: 만료x)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겼는지 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되었는지 (true: 만료x)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    // 계정이 활성화(사용가능)인지 (true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
