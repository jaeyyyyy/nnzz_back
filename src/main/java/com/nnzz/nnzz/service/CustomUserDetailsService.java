package com.nnzz.nnzz.service;

import com.nnzz.nnzz.config.security.SecurityUser;
import com.nnzz.nnzz.config.seed.Seed;
import com.nnzz.nnzz.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService; // 사용자 정보를 조회하는 서비스
    private final Seed seed;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userService.getOptionalUserByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다: " + email);
        }
        return createUser(user);
        //return new UserInfoDetails(user); // UserInfoDetails 객체 반환
    }

    private User createUser(UserDTO user) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new SecurityUser(user.getUserId(),
                user.getEmail(),
                "{noop}",
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);

    }

    private UserDetails loadUserDetails(UserDTO user) {
        return User.builder()
                .username(user.getEmail())
                //.username(seed.decrypt(user.getEmail()))
                .roles("ROLE_USER")
                .build();
    }

}
