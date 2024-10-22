package com.nnzz.nnzz.service;

import com.nnzz.nnzz.repository.FindStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class FindStoreService {
    private final FindStoreMapper findStoreMapper;

    @Autowired
    public FindStoreService(FindStoreMapper findStoreMapper) {
        this.findStoreMapper = findStoreMapper;
    }


    public List<String> getLunchCategoriesByLocation(double currentLat, double currentLong) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek();

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getLunchValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<String> validCategories = findStoreMapper.getCategories(validStoreIds);
        return validCategories;
    }


    public List<String> getDinnerCategoriesByLocation(double currentLat, double currentLong) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek();

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getDinnerValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<String> validCategories = findStoreMapper.getCategories(validStoreIds);
        return validCategories;
    }

    private String getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        String[] days = {"", "sun", "mon", "tue", "wed", "thu", "fri", "sat"};

        System.out.println("요일 : " + days[calendar.get(Calendar.DAY_OF_WEEK)]);
        return days[calendar.get(Calendar.DAY_OF_WEEK)];

    }
}
