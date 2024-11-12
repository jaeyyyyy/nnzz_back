package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.CategoryDTO;
import com.nnzz.nnzz.dto.StoreDTO;
import com.nnzz.nnzz.service.FindStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Tag(name="find stores", description = "음식점 찾기 API, 모든 api는 임의로 값을 넣어서 실행 중")
@RestController
@RequestMapping("/api/find")
public class FindStoreController {

    @Autowired
    private FindStoreService findStoreService;

    @Operation(summary = "get lunch categories", description = "직선거리 750m 내에 점심 영업중인 가게들의 카테고리 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/lunch/category")
    public List<CategoryDTO> getLunchCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        return findStoreService.getLunchCategoriesByLocation(lat, lng, day);
    }

    @Operation(summary = "get dinner categories", description = "직선거리 750m 내에 저녁 영업중인 가게들의 카테고리 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/dinner/category")
    public List<CategoryDTO> getDinnerCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        return findStoreService.getDinnerCategoriesByLocation(lat, lng, day);
    }

    @Operation(summary = "get lunch 750", description = "직선거리 750m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/lunch/detail/750")
    public ResponseEntity<List<StoreDTO>> get750LunchStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        List<String> storeIds = findStoreService.get750NearbyLunchStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 750", description = "직선거리 750m 내에 저녁 영업중인 가게들의 리스트 찾기 / 실행시간 테스트를 위해 모든 카테고리를 선택했다고 가정(750미터 저녁만)")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/dinner/detail/750")
    public ResponseEntity<List<StoreDTO>> get750DinnerStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        categoryList.add("양식");
        List<String> allCategoryList = new ArrayList<>(Arrays.asList("한식", "탕과 국", "찌개,전골", "백숙,삼계탕", "냉면", "국수와 만두", "샤브샤브", "죽", "족발,보쌈", "전,빈대떡", "중식",
                "일식", "초밥", "돈가스", "카레", "아시아음식", "분식", "양식", "멕시코,남미음식", "고기", "닭", "곱창,막창,양", "해물", "생선회", "피자", "햄버거", "빵", "술집", "뷔페", "다이어트,샐러드"));

        List<String> storeIds = findStoreService.get750NearbyDinnerStoreIds(lat, lng, day, allCategoryList);

//        for(String storeId : storeIds) {
//            System.out.println("storeIds 출력 : " + storeId);
//        }
        System.out.println("storeIds는 " + storeIds.size() + "개 입니다.");

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println("storeDetails는 " + storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }


    @Operation(summary = "get lunch 500", description = "직선거리 500m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/lunch/detail/500")
    public ResponseEntity<List<StoreDTO>> get500LunchStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        List<String> storeIds = findStoreService.get500NearbyLunchStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 500", description = "직선거리 500m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/dinner/detail/500")
    public ResponseEntity<List<StoreDTO>> get500DinnerStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        List<String> storeIds = findStoreService.get500NearbyDinnerStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get lunch 250", description = "직선거리 250m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/lunch/detail/250")
    public ResponseEntity<List<StoreDTO>> get250LunchStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        List<String> storeIds = findStoreService.get250NearbyLunchStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 250", description = "직선거리 250m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @GetMapping("/dinner/detail/250")
    public ResponseEntity<List<StoreDTO>> get250DinnerStoreDetail() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("한식");
        categoryList.add("중식");
        categoryList.add("일식");
        List<String> storeIds = findStoreService.get250NearbyDinnerStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get one store detail", description = "가게 1곳의 정보를 가져오기")
    @Parameters({
            @Parameter(name = "currentLat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "currentLong", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "storeId", description = "String 타입, 가게의 id", required = true)
    })
    @GetMapping("/store/{storeId}")
    public ResponseEntity<StoreDTO> getOneStoreDetail(@PathVariable("storeId") String storeId) {
        double lng = 127.0276241;
        double lat = 37.4979526;
        StoreDTO store = findStoreService.getOneStoreDetail(lat, lng, storeId);

        return ResponseEntity.ok(store);
    }
}
