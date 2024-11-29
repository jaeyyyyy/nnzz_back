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
    public UserDTO getUserByUserId(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    // 유저 찾기
    public UserDTO getUserByEmail(String email) {
        Optional<UserDTO> userByEmail = getOptionalUserByEmail(email);
        return userByEmail.orElse(null);
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

    public Optional<UserDTO> getOptionalUserByEmail(String email) {
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

    // 로그인
    public UserDTO login(UserDTO user) {
        Optional<UserDTO> userByEmail = getOptionalUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            // 이메일로 조회했을 때, 결과가 있다면
            return userByEmail.get();
        } else {
            // 조회결과가 없으면 리턴하지 않음
            return null;
        }
    }
}
