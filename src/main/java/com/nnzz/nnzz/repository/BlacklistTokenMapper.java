package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.BlacklistToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlacklistTokenMapper {
    void insertBlacklistToken(BlacklistToken blacklistToken);

    boolean existsByToken(@Param("token") String token);

    void deleteExpiredTokens();
}
