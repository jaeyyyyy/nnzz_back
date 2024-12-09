package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.SaveLocationRequest;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.exception.InvalidLocationException;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.service.UserLocationService;
import com.nnzz.nnzz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/location")
@RequiredArgsConstructor
public class UserLocationController {
    private final UserService userService;
    private final UserLocationService userLocationService;

    private static final double[][] STATIONS = {
            {37.4939732, 127.0146391}, // 교대역
            {37.4979526, 127.0276241}, // 강남역
            {37.5006431, 127.0363764}, // 역삼역
            {37.5045850, 127.0492805}, // 선릉역
            {37.5088803, 127.0631067} // 삼성역
    };


    @Operation(summary = "get user location", description = "<strong>\uD83D\uDCA1유저가 저장한 위치를 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 값으로 인한 조회 오류"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getUserLocations() {
        String email = SecurityUtils.getUserEmail();
        UserDTO authUser = userService.getUserByEmail(email);

        return ResponseEntity.ok(userLocationService.getUserLocations(authUser.getUserId()));
    }



    @Operation(summary = "save user location", description = "<strong>\uD83D\uDCA1유저의 위치를 db에 저장.</strong><br>최대 3개 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 저장 성공"),
            @ApiResponse(responseCode = "400", description = "오픈되지 않은 지역"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "address", description = "String 타입, 주소", required = true),
            @Parameter(name = "buildingName", description = "String 타입, 건물명", required = true)
    })
    @PostMapping("/save")
    public ResponseEntity<?> saveUserLocation(@RequestBody SaveLocationRequest request) {

        String email = SecurityUtils.getUserEmail();
        UserDTO authUser = userService.getUserByEmail(email);

        if(authUser == null) {
            throw new UnauthorizedException("유효한 사용자 정보가 없습니다."); // 이메일로 사용자 정보 조회 실패
        }

        double lat = request.getLat();
        double lng = request.getLng();
        String address = request.getAddress();
        String buildingName = request.getBuildingName();

        boolean withinAnyStation = false; // 조건 만족 여부를 추적하는 변수

        for (double[] station : STATIONS) {
            if(userLocationService.isWithinStation(lat, lng, station[0], station[1])) {
                userLocationService.saveUserLocation(authUser.getUserId(), lat, lng, address, buildingName);
                withinAnyStation = true; // 조건 만족 시 true로 설정
                break; // 조건 만족하면 더 이상 체크할 필요 없음
            }
        }

        if (!withinAnyStation) {
            throw new InvalidLocationException(lat, lng); // 모든 역에서 범위가 아닌 경우 예외 발생
        }

        return ResponseEntity.ok("위치가 성공적으로 저장되었습니다."); // 모든 역에서 범위가 아닌 경우 예외 발생
    }
}
