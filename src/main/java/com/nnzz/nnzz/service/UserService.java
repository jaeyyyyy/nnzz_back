package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    // 유저 찾기
    public Optional<UserDTO> getUser(Integer idx) {
        return userMapper.findUserByIdx(idx);
    }

    // 회원가입 처리
    public void registerUser(UserDTO user) {
        userMapper.createUser(user);
    }

    // 이메일 중복 찾기
    public boolean checkEmailExists(String email) {
        return userMapper.existsUserByEmail(email);
    }

    // 닉네임 중복 찾기
    public boolean checkNicknameExists(String nickname) {
        return userMapper.existsUserByNickname(nickname);
    }
}
