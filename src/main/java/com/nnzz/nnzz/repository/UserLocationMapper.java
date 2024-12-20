package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.UserLocationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserLocationMapper {
    List<UserLocationDTO> getUserLocations(int userId);

    int countUserLocations(int userId);

    void deleteOldestLocation(int userId);

    void insertUserLocation(@Param("userId") int userId, @Param("lat") double lat, @Param("lng") double lng, @Param("address") String address);

    void openUserLocation(@Param("userId") int userId, @Param("lat") double lat, @Param("lng") double lng, @Param("address") String address);

    boolean checkOpenUserRequest(@Param("userId") int userId, @Param("lat") double lat, @Param("lng") double lng, @Param("address") String address);
}
