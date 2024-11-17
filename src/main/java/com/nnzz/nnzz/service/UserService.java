package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public void registerUser(UserDTO user) {
        userMapper.createUser(user);
    }

    public boolean checkEmailExists(String email) {
        return userMapper.existsUserByEmail(email);
    }

    public boolean checkNicknameExists(String nickname) {
        return userMapper.existsUserByNickname(nickname);
    }
}
