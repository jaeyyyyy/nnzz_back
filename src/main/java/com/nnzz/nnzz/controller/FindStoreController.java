package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.service.FindStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @Parameter(name = "day", description = "아무 타입이든 괜찮으니 유저가 선택한 요일 정보를 넘겨주세요. 현재는 오늘 날짜만 가져오는 로직으로 설계되어 있습니다.", required = true),
    })
    @GetMapping("/lunch/category")
    public List<String> getLunchCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        List<String> lunchCategoryList = findStoreService.getLunchCategoriesByLocation(lat, lng);

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
            @Parameter(name = "day", description = "아무 타입이든 괜찮으니 유저가 선택한 요일 정보를 넘겨주세요. 현재는 오늘 날짜만 가져오는 로직으로 설계되어 있습니다.", required = true),
    })
    @GetMapping("/dinner/category")
    public List<String> getDinnerCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        List<String> dinnerCategoryList = findStoreService.getDinnerCategoriesByLocation(lat, lng);

        return dinnerCategoryList;
    }

}
