package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.CategoryDTO;
import com.nnzz.nnzz.dto.RequestDTO;
import com.nnzz.nnzz.dto.StoreDTO;
import com.nnzz.nnzz.service.FindStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="find stores", description = "음식점 찾기 API / (2024-11-27) 일부 API의 매핑 방식을 변경하고, 변수명을 수정")
@RestController
@RequestMapping("/api/find")
@RequiredArgsConstructor
public class FindStoreController {
    private final FindStoreService findStoreService;

    @Operation(summary = "get lunch categories", description = "직선거리 750m 내에 점심 영업중인 가게들의 카테고리 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/lunch/category")
    public ResponseEntity<List<CategoryDTO>> getLunchCategories(@RequestParam Double lng, @RequestParam Double lat, @RequestParam String day) {
        List<CategoryDTO> category = findStoreService.getLunchCategoriesByLocation(lat, lng, day);

        return ResponseEntity.ok(category);
    }

    @Operation(summary = "get dinner categories", description = "직선거리 750m 내에 저녁 영업중인 가게들의 카테고리 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/dinner/category")
    public ResponseEntity<List<CategoryDTO>> getDinnerCategories(@RequestParam Double lng, @RequestParam Double lat, @RequestParam String day) {
        List<CategoryDTO> category = findStoreService.getDinnerCategoriesByLocation(lat, lng, day);

        return ResponseEntity.ok(category);
    }

    @Operation(summary = "get lunch 750", description = "get lunch/dinner (거리값) api들은 categoryList(배열)을 파라미터로 필요로 해서 GET 방식이 아닌 POST 방식을 사용함. \n 직선거리 750m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트(배열) / \"categoryList\": [\"한식\",\"중식\",\"일식\"] 이런 식으로 보내주시면 됩니다. ", required = true)
    })
    @PostMapping("/lunch/detail/750")
    public ResponseEntity<List<StoreDTO>> get750LunchStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();

        List<String> storeIds = findStoreService.get750NearbyLunchStoreIds(lat, lng, day, categoryList);
        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 750", description = "직선거리 750m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트(배열) / \"categoryList\": [\"한식\",\"중식\",\"일식\"] 이런 식으로 보내주시면 됩니다. ", required = true)
    })
    @PostMapping("/dinner/detail/750")
    public ResponseEntity<List<StoreDTO>> get750DinnerStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();

        // List<String> allCategoryList = new ArrayList<>(Arrays.asList("한식", "탕과 국", "찌개,전골", "백숙,삼계탕", "냉면", "국수와 만두", "샤브샤브", "죽", "족발,보쌈", "전,빈대떡", "중식",
        //        "일식", "초밥", "돈가스", "카레", "아시아음식", "분식", "양식", "멕시코,남미음식", "고기", "닭", "곱창,막창,양", "해물", "생선회", "피자", "햄버거", "빵", "술집", "뷔페", "다이어트,샐러드"));

        List<String> storeIds = findStoreService.get750NearbyDinnerStoreIds(lat, lng, day, categoryList);

        // for(String storeId : storeIds) {
        // System.out.println("storeIds 출력 : " + storeId);
        // }
        // System.out.println("storeIds는 " + storeIds.size() + "개 입니다.");

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println("storeDetails는 " + storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }


    @Operation(summary = "get lunch 500", description = "직선거리 500m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/lunch/detail/500")
    public ResponseEntity<List<StoreDTO>> get500LunchStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();

        List<String> storeIds = findStoreService.get500NearbyLunchStoreIds(lat, lng, day, categoryList);
        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 500", description = "직선거리 500m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/dinner/detail/500")
    public ResponseEntity<List<StoreDTO>> get500DinnerStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();
        List<String> storeIds = findStoreService.get500NearbyDinnerStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get lunch 250", description = "직선거리 250m 내에 점심 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/lunch/detail/250")
    public ResponseEntity<List<StoreDTO>> get250LunchStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();
        List<String> storeIds = findStoreService.get250NearbyLunchStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get dinner 250", description = "직선거리 250m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/dinner/detail/250")
    public ResponseEntity<List<StoreDTO>> get250DinnerStoreDetail(@RequestBody RequestDTO request) {
        double lat = request.getLat();
        double lng = request.getLng();
        String day = request.getDay();
        List<String> categoryList = request.getCategoryList();
        List<String> storeIds = findStoreService.get250NearbyDinnerStoreIds(lat, lng, day, categoryList);

        List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
        // System.out.println(storeDetails.size() + "개 입니다.");
        return ResponseEntity.ok(storeDetails);
    }

    @Operation(summary = "get one store detail", description = "가게 1곳의 정보를 가져오기")
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "storeId", description = "String 타입, 가게의 id", required = true)
    })
    @GetMapping("/store")
    public ResponseEntity<StoreDTO> getOneStoreDetail(@RequestParam Double lng, @RequestParam Double lat, @RequestParam String storeId) {
        StoreDTO store = findStoreService.getOneStoreDetail(lat, lng, storeId);

        return ResponseEntity.ok(store);
    }
}
