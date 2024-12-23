package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.ResponseDetail;
import com.nnzz.nnzz.dto.SaveLocationRequest;
import com.nnzz.nnzz.exception.AlreadyValidLocationException;
import com.nnzz.nnzz.exception.InvalidLocationException;
import com.nnzz.nnzz.service.UserLocationService;
import com.nnzz.nnzz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/location")
@RequiredArgsConstructor
public class UserLocationController {
    private final UserService userService;
    private final UserLocationService userLocationService;
    private final SecurityUtils securityUtils;

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
        int authUserId = SecurityUtils.getUserId();

        return ResponseEntity.ok(userLocationService.getUserLocations(authUserId));
    }

    @Operation(summary = "open request", description = "<strong>\uD83D\uDCA1지역 오픈 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "오픈 요청 성공"),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 값",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "address", description = "String 타입, 주소", required = true)
    })
    @PostMapping("/open")
    public ResponseEntity<?> saveUserOpenRequest(@RequestBody SaveLocationRequest request, HttpServletRequest httpServletRequest) {
        int authUserId = SecurityUtils.getUserId();
        double lat = request.getLat();
        double lng = request.getLng();
        String address = request.getAddress();

        boolean withinAnyStation = false; // 조건 만족 여부를 추적하는 변수

        for (double[] station : STATIONS) {
            if(userLocationService.isWithinStation(lat, lng, station[0], station[1])) {
                withinAnyStation = true; // 조건 만족 시 true로 설정
                break; // 조건 만족하면 더 이상 체크할 필요 없음
            }
        }

        if (withinAnyStation) {
            throw new AlreadyValidLocationException(lat, lng); // 모든 역에서 범위가 아닐 때만 오픈 요청 가능
        } else {
            userLocationService.openUserLocation(authUserId, lat, lng, address);
            ResponseDetail responseDetail = userLocationService.returnOpenLocationResponse(httpServletRequest.getRequestURI());
            return ResponseEntity.ok(responseDetail);
        }
    }


    @Operation(summary = "save user location", description = "<strong>\uD83D\uDCA1유저의 위치를 db에 저장.</strong><br>최대 3개 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 저장 성공"),
            @ApiResponse(responseCode = "400", description = "오픈되지 않은 지역",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "lat", description = "double 타입, 사용자의 위도", required = true),
            @Parameter(name = "lng", description = "double 타입, 사용자의 경도", required = true),
            @Parameter(name = "address", description = "String 타입, 주소", required = true)
    })
    @PostMapping("/save")
    public ResponseEntity<?> saveUserLocation(@RequestBody SaveLocationRequest request, HttpServletRequest httpServletRequest) {

        int authUserId = SecurityUtils.getUserId();

        double lat = request.getLat();
        double lng = request.getLng();
        String address = request.getAddress();

        boolean withinAnyStation = false; // 조건 만족 여부를 추적하는 변수

        for (double[] station : STATIONS) {
            if(userLocationService.isWithinStation(lat, lng, station[0], station[1])) {
                userLocationService.saveUserLocation(authUserId, lat, lng, address);
                withinAnyStation = true; // 조건 만족 시 true로 설정
                break; // 조건 만족하면 더 이상 체크할 필요 없음
            }
        }

        if (!withinAnyStation) {
            throw new InvalidLocationException(lat, lng); // 모든 역에서 범위가 아닌 경우 예외 발생
        } else {
            ResponseDetail responseDetail = userLocationService.returnSaveLocationResponse(httpServletRequest.getRequestURI());
            return ResponseEntity.ok(responseDetail); // 모든 역에서 범위가 아닌 경우 예외 발생
        }
    }
}
