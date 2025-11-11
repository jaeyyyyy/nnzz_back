package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.security.SecurityUser;
import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.exception.FindStoreException;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.service.FindStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="find stores", description = "음식점 찾기 API / (2024-11-27) 일부 API의 매핑 방식을 변경하고, 변수명을 수정")
@RestController
@RequestMapping("/api/find")
@RequiredArgsConstructor
public class FindStoreController {
    private final FindStoreService findStoreService;

    @Operation(summary = "get lunch categories", description = "<strong>\uD83D\uDCA1카드액션 시작시 영업중인 점심 카드 가져오기</strong><br>(직선거리 750m 내에 점심 영업중인 가게들의 카테고리 찾기)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "400", description = "오픈되지 않은 지역",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "choice", description = "boolean 타입, 값이 없거나 false: 30개 카테고리 전부, true: 15개 카테고리를 랜덤으로", required = false),
    })
    @GetMapping("/lunch/category")
    public ResponseEntity<List<CategoryDTO>> getLunchCategories(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam Double lng, @RequestParam Double lat, @RequestParam String day,
            @RequestParam(defaultValue = "false") boolean choice) {

        if(user == null) {
            // 인증되지 않은 사용자
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            List<CategoryDTO> category;
            if(choice) {
                // 가능한 카테고리들 중에서 15개를 랜덤으로 보여줍니다
                category = findStoreService.getLunchCategoriesByLocationAndChoice(lat, lng, day);
            } else {
                // 가능한 카테고리들 전부를 보여주는 기존 코드
                category = findStoreService.getLunchCategoriesByLocation(lat, lng, day);
            }
            return ResponseEntity.ok(category);
        }
    }

    @Operation(summary = "get dinner categories", description = "<strong>\uD83D\uDCA1카드액션 시작시 영업중인 저녁 카드 가져오기</strong><br>(직선거리 750m 내에 저녁 영업중인 가게들의 카테고리 찾기)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "400", description = "오픈되지 않은 지역",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
            // ,@ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
            //      content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "choice", description = "boolean 타입, 값이 없거나 false: 30개 카테고리 전부, true: 15개 카테고리를 랜덤으로", required = false),
    })
    @GetMapping("/dinner/category")
    public ResponseEntity<List<CategoryDTO>> getDinnerCategories(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam Double lng, @RequestParam Double lat, @RequestParam String day,
            @RequestParam(defaultValue = "false") boolean choice) {

        if(user == null) {
            // 인증되지 않은 사용자도 조회 가능
            // throw new UnauthorizedException("인증되지 않은 사용자입니다.");
            List<CategoryDTO> category;
            if(choice) {
                // 가능한 카테고리들 중에서 15개를 랜덤으로 보여줌
                category = findStoreService.getDinnerCategoriesByLocationAndChoice(lat, lng, day);
            } else {
                // 가능한 카테고리들 전부를 보여주는 기존 코드
                category = findStoreService.getDinnerCategoriesByLocation(lat, lng, day);
            }
            return ResponseEntity.ok(category);
        } else {
            List<CategoryDTO> category;
            if(choice) {
                // 가능한 카테고리들 중에서 15개를 랜덤으로 보여줌
                category = findStoreService.getDinnerCategoriesByLocationAndChoice(lat, lng, day);
            } else {
                // 가능한 카테고리들 전부를 보여주는 기존 코드
                category = findStoreService.getDinnerCategoriesByLocation(lat, lng, day);
            }
            return ResponseEntity.ok(category);
        }
    }

    @Operation(summary = "get random lunch category", description = "<strong>\uD83D\uDCA1점심 영업중인 카드 랜덤하게 가져오기</strong><br>(직선거리 750m 내에 점심 영업중인 가게중 랜덤 카테고리 하나 고르기)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/lunch/category/random")
    public ResponseEntity<CategoryDTO> getRandomLunchCategory(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam Double lng, @RequestParam Double lat, @RequestParam String day) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            CategoryDTO category = findStoreService.getLunchRandomCategory(lat, lng, day);
            return ResponseEntity.ok(category);
        }
    }

    @Operation(summary = "get random dinner category", description = "<strong>\uD83D\uDCA1저녁 영업중인 카드 랜덤하게 가져오기</strong><br>(직선거리 750m 내에 저녁 영업중인 가게중 랜덤 카테고리 하나 고르기)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
    })
    @GetMapping("/dinner/category/random")
    public ResponseEntity<CategoryDTO> getRandomDinnerCategory(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam Double lng, @RequestParam Double lat, @RequestParam String day) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            CategoryDTO category = findStoreService.getDinnerRandomCategory(lat, lng, day);
            return ResponseEntity.ok(category);
        }
    }


    @Operation(summary = "get lunch 750",
            description = "<strong>\uD83D\uDCA1카드액션 선택이 끝난 후 점심 식당 정보 보러가기(디폴트 값 = 750m)</strong><br>get lunch/dinner (거리값) api들은 categoryList(배열)을 파라미터로 필요로 해서 GET 방식이 아닌 POST 방식을 사용함.<br>직선거리 750m 내에 점심 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트(배열) / \"categoryList\": [\"한식\",\"중식\",\"일식\"] 이런 식으로 보내주시면 됩니다. ", required = true)
    })
    @PostMapping("/lunch/detail/750")
    public ResponseEntity<List<StoreDTO>> get750LunchStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();

            List<String> storeIds = findStoreService.get750NearbyLunchStoreIds(lat, lng, day, categoryList);
            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);
            // System.out.println(storeDetails.size() + "개 입니다.");
            return ResponseEntity.ok(storeDetails);
        }
    }

    @Operation(summary = "get dinner 750", description = "<strong>\uD83D\uDCA1카드액션 선택이 끝난 후 저녁 식당 정보 보러가기(디폴트 값 = 750m)</strong><br>직선거리 750m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트(배열) / \"categoryList\": [\"한식\",\"중식\",\"일식\"] 이런 식으로 보내주시면 됩니다. ", required = true)
    })
    @PostMapping("/dinner/detail/750")
    public ResponseEntity<List<StoreDTO>> get750DinnerStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();

            // List<String> allCategoryList = new ArrayList<>(Arrays.asList("한식", "탕과 국", "찌개,전골", "백숙,삼계탕", "냉면", "국수와 만두", "샤브샤브", "죽", "족발,보쌈", "전,빈대떡", "중식",
            //        "일식", "초밥", "돈가스", "카레", "아시아음식", "분식", "양식", "멕시코,남미음식", "고기", "닭", "곱창,막창,양", "해물", "생선회", "피자", "햄버거", "빵", "술집", "뷔페", "다이어트,샐러드"));

            List<String> storeIds = findStoreService.get750NearbyDinnerStoreIds(lat, lng, day, categoryList);

            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);

            return ResponseEntity.ok(storeDetails);
        }
    }


    @Operation(summary = "get lunch 500", description = "<strong>\uD83D\uDCA1결과 표기 화면에서 거리를 500m로 바꿀 경우(점심)</strong><br>직선거리 500m 내에 점심 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/lunch/detail/500")
    public ResponseEntity<List<StoreDTO>> get500LunchStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();

            List<String> storeIds = findStoreService.get500NearbyLunchStoreIds(lat, lng, day, categoryList);
            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);

            return ResponseEntity.ok(storeDetails);
        }
    }

    @Operation(summary = "get dinner 500", description = "<strong>\uD83D\uDCA1결과 표기 화면에서 거리를 500m로 바꿀 경우(저녁)</strong><br>직선거리 500m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "Integer 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/dinner/detail/500")
    public ResponseEntity<List<StoreDTO>> get500DinnerStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();
            List<String> storeIds = findStoreService.get500NearbyDinnerStoreIds(lat, lng, day, categoryList);

            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);

            return ResponseEntity.ok(storeDetails);
        }
    }

    @Operation(summary = "get lunch 250", description = "<strong>\uD83D\uDCA1결과 표기 화면에서 거리를 250m로 바꿀 경우(점심)</strong><br>직선거리 250m 내에 점심 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/lunch/detail/250")
    public ResponseEntity<List<StoreDTO>> get250LunchStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();
            List<String> storeIds = findStoreService.get250NearbyLunchStoreIds(lat, lng, day, categoryList);

            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);

            return ResponseEntity.ok(storeDetails);
        }
    }

    @Operation(summary = "get dinner 250", description = "<strong>\uD83D\uDCA1결과 표기 화면에서 거리를 250m로 바꿀 경우(저녁)</strong><br>직선거리 250m 내에 저녁 영업중인 가게들의 리스트 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가능한 카테고리 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "day", description = "String 타입 'yyyy-MM-dd' 형태, 사용자가 선택한 날짜", required = true),
            @Parameter(name = "categoryList", description = "String 타입, 사용자가 선택한 카테고리 리스트", required = true)
    })
    @PostMapping("/dinner/detail/250")
    public ResponseEntity<List<StoreDTO>> get250DinnerStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody FindStoreRequest request) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            double lat = request.getLat();
            double lng = request.getLng();
            String day = request.getDay();
            List<Integer> categoryList = request.getCategoryList();
            List<String> storeIds = findStoreService.get250NearbyDinnerStoreIds(lat, lng, day, categoryList);

            List<StoreDTO> storeDetails = findStoreService.getFinalStoresWithMenuAndBroadcast(lat, lng, storeIds);

            return ResponseEntity.ok(storeDetails);
        }
    }

    @Operation(summary = "get one store detail", description = "<strong>\uD83D\uDCA1결과표기 화면에서 특정 가게 1곳 클릭할 경우</strong><br>가게 1곳의 정보를 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 조회 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 lat, lng, storeId 값",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "storeId", description = "String 타입, 가게의 id", required = true)
    })
    @GetMapping("/store")
    public ResponseEntity<OneStoreDTO> getOneStoreDetail(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam Double lng, @RequestParam Double lat, @RequestParam String storeId) {
        if(user == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        } else {
            // 입력값 검증
            if (lng == null || lat == null || storeId == null || storeId.isEmpty()) {
                throw new FindStoreException("lat, lng, storeId 값은 필수입니다.");
            }

            OneStoreDTO store = findStoreService.getOneStoreDetail(lat, lng, storeId);

            if (store == null) {
                throw new FindStoreException("존재하지 않는 storeId 값입니다.");
            }

            return ResponseEntity.ok(store);
        }
    }
}
