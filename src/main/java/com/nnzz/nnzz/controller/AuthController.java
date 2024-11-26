package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.service.AuthService;
import com.nnzz.nnzz.service.OauthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="kakao oauth", description = "회원가입 후 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {
    private final AuthService authService;
    private final OauthUserService oauthUserService;

    // 여기서 로그인과 회원가입을 마치고
//    @Operation(summary = "kakao oauth login", description = "카카오 oauth에서 받아온 정보를 db에 우선 저장")
//    @ApiResponses(value={
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
//    })
//    @Parameters({
//
//    })
    @GetMapping("/auth/login")
    public ResponseEntity<String> loginKakao(@RequestParam("code") String accessCode, @PathVariable String reqistration) {
        // String loginMessage = oauthUserService.
        return null;
    }
}
