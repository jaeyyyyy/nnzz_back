package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);

    void updateUser(UserDTO user);

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
