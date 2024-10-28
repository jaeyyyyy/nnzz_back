package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.BroadcastDTO;
import com.nnzz.nnzz.dto.MenuDTO;
import com.nnzz.nnzz.dto.StoreDTO;
import com.nnzz.nnzz.repository.FindStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FindStoreService {
    private final FindStoreMapper findStoreMapper;

    @Autowired
    public FindStoreService(FindStoreMapper findStoreMapper) {
        this.findStoreMapper = findStoreMapper;
    }

    // 1. 현재 선택 가능한 카테고리를 가져오기
    // 점심 가능 카테고리 가져오기
    public List<String> getLunchCategoriesByLocation(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getLunchValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<String> validCategories = findStoreMapper.getCategories(validStoreIds);
        return validCategories;
    }

    // 저녁 가능 카테고리 가져오기
    public List<String> getDinnerCategoriesByLocation(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getDinnerValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<String> validCategories = findStoreMapper.getCategories(validStoreIds);
        return validCategories;
    }

    // 날짜 가져오기
    private String getCurrentDayOfWeek(String dateString) {
        // SimpleDateFormat을 사용하여 문자열을 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateString);

            // Date를 Calendar로 변환
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String[] days = {"", "sun", "mon", "tue", "wed", "thu", "fri", "sat"};

            System.out.println("요일 : " + days[calendar.get(Calendar.DAY_OF_WEEK)]);
            return days[calendar.get(Calendar.DAY_OF_WEEK)];
        } catch(ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. 가능한 가게 store_id를 가져오기
    // 750 점심
    public List<String> get750NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores750NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            return new ArrayList<>(); // 빈 리스트 반환
        }
        List<String> validLunchStoreIds = findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);

        return validLunchStoreIds;
    }

    // 750 저녁
    public List<String> get750NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores750NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
//        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
//            return new ArrayList<>(); // 빈 리스트 반환
//        }
        List<String> validDinnerStoreIds = findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);

        return validDinnerStoreIds;
    }


    // 500 점심
    public List<String> get500NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores500NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            return new ArrayList<>(); // 빈 리스트 반환
        }
        List<String> validLunchStoreIds = findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);

        return validLunchStoreIds;
    }

    // 500 저녁
    public List<String> get500NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores500NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            return new ArrayList<>(); // 빈 리스트 반환
        }
        List<String> validDinnerStoreIds = findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);

        return validDinnerStoreIds;
    }


    // 250 점심
    public List<String> get250NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores250NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            return new ArrayList<>(); // 빈 리스트 반환
        }
        List<String> validLunchStoreIds = findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);

        return validLunchStoreIds;
    }

    // 250 저녁
    public List<String> get250NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<String> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores250NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            return new ArrayList<>(); // 빈 리스트 반환
        }
        List<String> validDinnerStoreIds = findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);

        return validDinnerStoreIds;
    }

    // 최종 가게 리스트 반환하기
    public List<StoreDTO> getStoreDetails(double currentLat, double currentLong, List<String> storeIds) {
        // 기본 가게 정보 가져오기
        List<StoreDTO> stores = findStoreMapper.getFinalStores(currentLat, currentLong, storeIds);

        // 각 가게에 대해 메뉴와 방송 정보 추가
        for (int i = 0; i < stores.size(); i++) {
            StoreDTO store = stores.get(i); // 현재 StoreDTO 가져오기

            System.out.println(store);
            System.out.println(store.getStoreId());
            List<MenuDTO> menus = findStoreMapper.getMenuInfo(store.getStoreId());
            List<BroadcastDTO> broadcasts = findStoreMapper.getBroadcastInfo(store.getStoreId());
            String category = findStoreMapper.getCategory(store.getStoreId());

            // Builder 패턴을 사용하여 StoreDTO 생성
            stores.set(i, StoreDTO.builder()  // 원래 리스트의 해당 인덱스에 새로운 객체 설정
                    .storeId(store.getStoreId())
                    .name(store.getName())
                    .address(store.getAddress())
                    .distance(store.getDistance())
                    .category(category)
                    .menus(menus)
                    .broadcasts(broadcasts)
                    .build());
        }

        return stores;
    }
}
