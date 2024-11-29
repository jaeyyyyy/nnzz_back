package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.dto.UserInfoDetails;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userMapper.findUserByEmail(email);
        return new UserInfoDetails(user);
    }

    private UserDTO getUser(String email) {
        return userMapper.findUserByEmail(email);
        // UserDTO user =
    }
}
