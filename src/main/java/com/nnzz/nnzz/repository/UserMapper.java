package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);

    boolean existsUserByEmail(String email);

    boolean existsUserByNickname(String nickname);
}
