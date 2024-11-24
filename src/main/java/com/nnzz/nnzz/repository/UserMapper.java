package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);

    void updateUser(UserDTO user);

    boolean existsUserByEmail(String email);

    boolean existsUserByNickname(String nickname);

    boolean existsUserByUserId(int userId);

    Optional<UserDTO> findUserByUserId(int userId);
}
