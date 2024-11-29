package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    // 유저 찾기
    public UserDTO getUser(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    // 회원정보 수정
    public void updateUser(UserDTO user){
        userMapper.updateUser(user);
    }

    // 회원가입 처리
    public void registerUser(UserDTO user) {
        userMapper.createUser(user);
    }

    // 이메일 중복 찾기
    public boolean checkEmailExists(String email) {
        return userMapper.existsUserByEmail(email);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    // 닉네임 중복 찾기
    public boolean checkNicknameExists(String nickname) {
        return userMapper.existsUserByNickname(nickname);
    }

    // 실제 있는 userId인지 확인
    public boolean checkUserIdExists(int userId) {
        return userMapper.existsUserByUserId(userId);
    }
}
