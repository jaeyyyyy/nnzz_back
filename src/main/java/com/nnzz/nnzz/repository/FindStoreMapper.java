package com.nnzz.nnzz.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FindStoreMapper {
    // 1단계: 반경 750m, 500m, 250m 안의 store_id 가져오기
    List<String> get750NearbyStoreIds(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong);

    List<String> get500NearbyStoreIds(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong);

    List<String> get250NearbyStoreIds(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong);


    // 2단계: business_hours에서 점심 조건(12시)에 맞는 store_id 가져오기
    List<String> getLunchValidStoreIds(@Param("storeIds") List<String> storeIds, @Param("currentDay") String currentDay);

    // 2단계: business_hours에서 저녁 조건(6시)에 맞는 store_id 가져오기
    List<String> getDinnerValidStoreIds(@Param("storeIds") List<String> storeIds, @Param("currentDay") String currentDay);

    // 3단계: 최종적으로 category 가져오기
    List<String> getCategories(@Param("storeIds") List<String> storeIds);
}
