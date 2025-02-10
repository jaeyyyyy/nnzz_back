package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.exception.FindStoreException;
import com.nnzz.nnzz.repository.FindStoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FindStoreService {
    private final FindStoreMapper findStoreMapper;

    // 날짜 가져오기
    private String getCurrentDayOfWeek(String dateString) throws DateTimeParseException {
        // SimpleDateFormat을 사용하여 문자열을 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // 엄격한 날짜 형식 검사를 활성화
        try {
            Date date = dateFormat.parse(dateString);

            // Date를 Calendar로 변환
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String[] days = {"", "sun", "mon", "tue", "wed", "thu", "fri", "sat"};

            System.out.println("요일 : " + days[calendar.get(Calendar.DAY_OF_WEEK)]);
            return days[calendar.get(Calendar.DAY_OF_WEEK)];
        } catch(ParseException e) {
            throw new DateTimeParseException("날짜 : " + dateString +"유효하지 않은 날짜 형식입니다.", dateString, 0);
        }
    }

    // 카테고리를 랜덤으로 count 개수만큼 뽑기
    private List<CategoryDTO> getRandomCategories(List<CategoryDTO> categories, int count) {
        // 전체 목록의 크기가 count보다 작으면 가능한 최대만큼 가져옴
        int size = Math.min(count, categories.size());

        // 랜덤으로 아이템 선택
        Collections.shuffle(categories);
        return new ArrayList<>(categories.subList(0, size));
    }

    // 점심 랜덤 카테고리 1개 뽑기
    public CategoryDTO getLunchRandomCategory(double currentLat, double currentLong, String dateString) {
        List<CategoryDTO> lunchCategories = getLunchCategoriesByLocation(currentLat, currentLong, dateString);
        if(lunchCategories.isEmpty()) { // 리스트가 비어있지 않은지 확인
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(lunchCategories.size());
        // 랜덤으로 선택된 CategoryDTO 반환
        return lunchCategories.get(randomIndex);
    }

    // 저녁 랜덤 카테고리 뽑기
    public CategoryDTO getDinnerRandomCategory(double currentLat, double currentLong, String dateString) {
        List<CategoryDTO> dinnerCategories = getDinnerCategoriesByLocation(currentLat, currentLong, dateString);
        if(dinnerCategories.isEmpty()) { // 리스트가 비어있지 않은지 확인
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(dinnerCategories.size());
        // 랜덤으로 선택된 CategoryDTO 반환
        return dinnerCategories.get(randomIndex);
    }

    // 1. 현재 선택 가능한 카테고리를 가져오기
    // 점심 가능 카테고리 가져오기(choice=false)
    public List<CategoryDTO> getLunchCategoriesByLocation(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        if(nearByStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getLunchValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        return findStoreMapper.getCategories(currentLat, currentLong, validStoreIds);
    }

    // 점심 가능 카테고리 가져오기(choice = true)
    public List<CategoryDTO> getLunchCategoriesByLocationAndChoice(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        if(nearByStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getLunchValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<CategoryDTO> allCategories = findStoreMapper.getCategories(currentLat, currentLong, validStoreIds);

        // 랜덤으로 15개 아이템 선택
        return getRandomCategories(allCategories, 15);
    }


    // 저녁 가능 카테고리 가져오기(choice = false)
    public List<CategoryDTO> getDinnerCategoriesByLocation(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        if(nearByStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getDinnerValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        return findStoreMapper.getCategories(currentLat, currentLong, validStoreIds);
    }

    // 저녁 가능 카테고리 가져오기(choice = true)
    public List<CategoryDTO> getDinnerCategoriesByLocationAndChoice(double currentLat, double currentLong, String dateString) {
        // 1단계 : 반경 750m 안의 store_id 값 가져오기
        List<String> nearByStoreIds = findStoreMapper.get750NearbyStoreIds(currentLat, currentLong);

        if(nearByStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : business_hours에서 조건에 맞는 store_id 가져오기
        List<String> validStoreIds = findStoreMapper.getDinnerValidStoreIds(nearByStoreIds, currentDay);

        // 3단계 : 최종적으로 category 가져오기
        List<CategoryDTO> allCategories = findStoreMapper.getCategories(currentLat, currentLong, validStoreIds);
        return getRandomCategories(allCategories, 15);
    }

    // 2. 가능한 가게 store_id를 가져오기
    // 750 점심
    public List<String> get750NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores750NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);
    }

    // 750 저녁
    public List<String> get750NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores750NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);
    }


    // 500 점심
    public List<String> get500NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores500NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);
    }

    // 500 저녁
    public List<String> get500NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores500NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);
    }


    // 250 점심
    public List<String> get250NearbyLunchStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores250NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getLunchValidStoreIds(nearbyStoreIds, currentDay);
    }

    // 250 저녁
    public List<String> get250NearbyDinnerStoreIds(double currentLat, double currentLong, String dateString, List<Integer> categories) {
        // 1단계 : 750내의 선택한 카테고리 가게의 store_id 가져오기
        List<String> nearbyStoreIds = findStoreMapper.getStores250NearbyAndByCategory(currentLat, currentLong, categories);

        // 오늘의 요일 가져오기
        String currentDay = getCurrentDayOfWeek(dateString);

        // 2단계 : 점심영업중인 가게의 store_id 가져오기
        if(nearbyStoreIds.isEmpty()) { // 만약 store_id가 없다면
            throw new FindStoreException("선택한 카테고리의 가게가 없습니다.");
        }

        return findStoreMapper.getDinnerValidStoreIds(nearbyStoreIds, currentDay);
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

    // 전체 가게 정보 한 번에 가져오기
    public List<StoreDTO> getFinalStoresWithMenuAndBroadcast(double currentLat, double currentLong, List<String> storeIds) {

        return findStoreMapper.getFinalStoresDetail(currentLat, currentLong, storeIds);
    }

    public OneStoreDTO getOneStoreDetail(double currentLat, double currentLong, String storeId) {
        if (storeId == null || storeId.isEmpty()) {
            return null;
        }

        return findStoreMapper.getOneStoreDetail(currentLat, currentLong, storeId);
    }
}
