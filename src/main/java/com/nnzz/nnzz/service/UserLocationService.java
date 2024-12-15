package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.UserLocationDTO;
import com.nnzz.nnzz.exception.AlreadyRequestedLocationException;
import com.nnzz.nnzz.repository.UserLocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationService {
    private final UserLocationMapper userLocationMapper;

    public boolean isWithinStation(double lat1, double lng1, double lat2, double lng2) {
        double distance = calculateDistance(lat1, lng1, lat2, lng2);
        return distance <= 750;
    }

    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // 하버사인 공식 사용
        final int R = 6371; // 지구 반지름(km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // km를 m로 변환
        return distance;
    }


    public List<UserLocationDTO> getUserLocations(int userId) {
        return userLocationMapper.getUserLocations(userId);
    }

    public void saveUserLocation(int userId, double lat, double lng, String address, String buildingName) {
        // 1. 현재 저장된 위치 수 확인
        int count = userLocationMapper.countUserLocations(userId);

        // 2. 3개 이상일 경우 가장 오래된 위치 삭제
        if (count >= 3) {
            userLocationMapper.deleteOldestLocation(userId);
        }
        // 3. 새로운 위치 저장
        userLocationMapper.insertUserLocation(userId, lat, lng, address, buildingName);
    }

    public void openUserLocation(int userId, double lat, double lng, String address, String buildingName) {
        // 현재 똑같은 오픈 요청이 들어와있는지 확인
        boolean check = userLocationMapper.checkOpenUserRequest(userId, lat, lng, address, buildingName);
        if(check) {
            throw new AlreadyRequestedLocationException(lat, lng);
        } else {
            // 오픈요청이 들어와있지 않은 경우에 저장
            userLocationMapper.openUserLocation(userId, lat, lng, address, buildingName);
        }

    }
}
