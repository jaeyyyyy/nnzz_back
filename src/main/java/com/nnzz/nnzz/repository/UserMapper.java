package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);

    void updateUser(UserDTO user);

    // 회원정보 수정(업데이트 중)
    void updateUserProfileImage(@Param("profileImage") String profileImage, @Param("userId") Integer userId);
    void updateUserNickname(@Param("nickname") String nickname, @Param("userId") Integer userId);
    void updateUserAgeAndGender(@Param("gender") String gender, @Param("ageRange") String ageRange, @Param("userId") Integer userId);

    boolean existsUserByEmail(String email);

    boolean existsUserByNickname(@Param("nickname") String nickname, @Param("userId") Integer userId);

    boolean existsUserByUserId(int userId);

    UserDTO findUserByUserId(int userId);
    UserDTO findUserByEmail(String email);
    UserDTO findUserByLoginId(String loginId);

    @Select("SELECT * FROM users WHERE email=#{email}")
    Optional<UserDTO> getUserByEmail(String email);

    @Select("SELECT * FROM users WHERE user_id=#{userId}")
    Optional<UserDTO> getUserByUserId(int userId);

    @Select("DELETE FROM users WHERE user_id=#{userId}")
    void deleteUserByUserId(int userId);
}
