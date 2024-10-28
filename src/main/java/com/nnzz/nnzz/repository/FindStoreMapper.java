package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.BroadcastDTO;
import com.nnzz.nnzz.dto.MenuDTO;
import com.nnzz.nnzz.dto.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FindStoreMapper {
    // 가능한 카테고리를 반환하기
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



    // 가능한 가게 리스트를 반환하기
    // 카테고리와 거리에 해당하는 가게 리스트 반환하기
    List<String> getStores750NearbyAndByCategory(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong, @Param("categories") List<String> categories);

    List<String> getStores500NearbyAndByCategory(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong, @Param("categories") List<String> categories);

    List<String> getStores250NearbyAndByCategory(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong, @Param("categories") List<String> categories);


    // 최종 가게 정보 불러오기
    List<StoreDTO> getFinalStores(@Param("currentLat") double currentLat, @Param("currentLong") double currentLong, @Param("storeIds") List<String> storeIds);

    // 메뉴 가져오기
    List<MenuDTO> getMenuInfo(@Param("storeId") String storeId);

    // 방송 가져오기
    List<BroadcastDTO> getBroadcastInfo(@Param("storeId") String storeId);

    // 카테고리 가져오기
    String getCategory(@Param("storeId") String storeId);
}
