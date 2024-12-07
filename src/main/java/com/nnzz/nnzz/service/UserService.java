package com.nnzz.nnzz.service;

import com.nnzz.nnzz.config.seed.Seed;
import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.exception.NicknameUpdateException;
import com.nnzz.nnzz.exception.UserNotExistsException;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;
    private final Seed seed;

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
    @Transactional
    public void updateUser(UserDTO user){
        UserDTO existingUser = getUserByUserId(user.getUserId());

        // 기존 닉네임을 전달
        UserDTO oldUser = UserDTO.builder()
                .userId(existingUser.getUserId())
                .email(existingUser.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .lastNicknameChangeDate(existingUser.getLastNicknameChangeDate())
                .build();


        // 닉네임이 변경되었는지 확인
        if(!existingUser.getNickname().equals(user.getNickname())){
            LocalDateTime lastChangeDate = existingUser.getLastNicknameChangeDate();
            LocalDateTime now = LocalDateTime.now();

            // 닉네임 변경 가능 여부 체크
            if(lastChangeDate != null){
                long daysBetween = ChronoUnit.DAYS.between(lastChangeDate, now);
                if(daysBetween < 30){
                    throw new NicknameUpdateException(user.getNickname());
                }
            }
        }
        userMapper.updateUser(oldUser);
    }

    // 회원가입 처리
    @Transactional
    public void registerUser(UserDTO user) {
        UserDTO encryptedUser = UserDTO.builder()
                .email(seed.encrypt(user.getEmail()))
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .build();
        userMapper.createUser(encryptedUser);
    }

    // 이메일 중복 찾기
    public boolean checkEmailExists(String email) {
        return userMapper.existsUserByEmail(seed.encrypt(email));
    }

    public Optional<UserDTO> getOptionalUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public Optional<UserDTO> getOptionalUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }


    // 닉네임 중복 찾기
    public boolean checkNicknameExists(String nickname, Integer userId) {
        return userMapper.existsUserByNickname(nickname, userId);
    }

    // 실제 있는 userId인지 확인
    public boolean checkUserIdExists(int userId) {
        return userMapper.existsUserByUserId(userId);
    }

    // 로그인
    public UserDTO login(UserDTO user) {
        Optional<UserDTO> userByEmail = getOptionalUserByEmail(seed.encrypt(user.getEmail()));
        if (userByEmail.isPresent()) {
            // 이메일로 조회했을 때, 결과가 있다면
            return userByEmail.get();
        } else {
            // 조회결과가 없으면 리턴하지 않음
            return null;
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Integer userId) {
        // 사용자 찾기
        UserDTO userByUserId = getOptionalUserByUserId(userId)
                .orElseThrow(() -> new UserNotExistsException(userId));
        // 사용자 삭제
        userMapper.deleteUserByUserId(userId);
    }
}
