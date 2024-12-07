package com.nnzz.nnzz.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserInfoDetails implements UserDetails {
    private final UserDTO user;

    public UserInfoDetails(UserDTO user) {
        this.user = user;
    }

    // 모든 사용자 정보를 반환하는 메서드
    public UserDTO getUserInfo() {
        return user;
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
