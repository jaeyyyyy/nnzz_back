package com.nnzz.nnzz.oauth;

import com.nnzz.nnzz.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OauthUserDetails implements UserDetails, OAuth2User {

    private final UserDTO user;
    private final Map<String, Object> attributes;


    public OauthUserDetails(UserDTO user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getEmail() {
        return user.getEmail();
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
    public Map<String, Object> getAttributes() {
        return attributes;
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
