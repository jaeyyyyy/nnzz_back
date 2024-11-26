package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="users", description = "냠냠쩝쩝 회원 추가 설정 및 회원 관리")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 여기에는 냠냠쩝쩝에서 설정가능한 정보를 넣을 예정임
    @Operation(summary = "register user", description = "회원 정보를 db에 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 닉네임 사용"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "email", description = "카카오 oauth에서 받아온 유저의 이메일", required = true),
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지. 1,2,3... 같은 int 값을 저장하고 그 값에 해당하는 이미지 주소는 따로 링크시키는 방법을 생각 중", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user) {

        if (userService.checkEmailExists(user.getEmail())) {
            // 이메일이 존재하지 않으면 사용자 추가
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 이메일입니다.");
        } else if (userService.checkNicknameExists(user.getNickname())) {
            // 중복된 닉네임일 경우 추가하지 않음
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        } else {
            userService.registerUser(user);
            return ResponseEntity.ok("사용자가 성공적으로 추가되었습니다.");
        }
    }

    // 회원 정보 업데이트
    @Operation(summary = "update user", description = "회원정보를 db에 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "경로의 회원 pk(userId)와 본문의 회원 pk가 일치하지 않음"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 또는 존재하지 않는 유저")
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 pk(userId, 자동증가 Integer 형태)", required = true),
            @Parameter(name = "nickname", description = "닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PatchMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO user, @PathVariable Integer userId) {
        // 요청 본문에서 가져온 닉네임
        String nickname = user.getNickname();
        // 요청 본문에서 가져온 userId
        Integer requestUserId = user.getUserId();

        if(!userId.equals(requestUserId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("경로의 userId와 본문에 있는 userId가 일치하지 않습니다.");
        }

        if(userService.checkUserIdExists(userId)) {
            if(userService.checkNicknameExists(nickname)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 닉네임입니다.");
            }
            userService.updateUser(user);
            return ResponseEntity.ok("사용자가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("존재하지 않는 사용자입니다.");
        }
    }

        // 닉네임 중복여부만 체크
    @Operation(summary = "check nickname duplicate", description = "중복된 닉네임인지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 사용"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
    })
    @GetMapping("/check")
    public ResponseEntity<String> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = userService.checkNicknameExists(nickname);

        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 닉네임입니다.");
        }
    }
}
