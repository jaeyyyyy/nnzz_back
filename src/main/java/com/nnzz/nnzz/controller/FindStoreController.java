package com.nnzz.nnzz.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name="find categories", description = "카테고리 불러오기 API")
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
    public List<String> getLunchCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> lunchCategoryList = findStoreService.getLunchCategoriesByLocation(lat, lng, day);

        return lunchCategoryList;
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
    public List<String> getDinnerCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        String day = "2024-11-01";
        List<String> dinnerCategoryList = findStoreService.getDinnerCategoriesByLocation(lat, lng, day);

        return dinnerCategoryList;
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

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 750", description = "직선거리 750m 내에 저녁 영업중인 가게들의 리스트 찾기")
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
        List<String> storeIds = findStoreService.get750NearbyDinnerStoreIds(lat, lng, day, categoryList);

        for(String storeId : storeIds) {
            System.out.println("storeIds 출력 : " + storeId);
        }
        System.out.println("storeIds는 " + storeIds.size() + "개 입니다.");

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
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

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
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

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
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

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
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

        List<StoreDTO> storeDetails = findStoreService.getStoreDetails(lat, lng, storeIds);
        System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }
}
