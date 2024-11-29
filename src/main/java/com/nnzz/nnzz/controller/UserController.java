package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.LoginRequestDTO;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.exception.*;
import com.nnzz.nnzz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Tag(name="users", description = "냠냠쩝쩝 회원 추가 설정 및 회원 관리")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // 여기에는 냠냠쩝쩝에서 설정가능한 정보를 넣을 예정임
    @Operation(summary = "register user", description = "회원 정보를 db에 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 회원가입한 유저, 올바르지 않은 형식의 닉네임"),
            @ApiResponse(responseCode = "409", description = "이미 사용중인 닉네임"),
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
    public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {

        // 한글, 영어, 숫자만 가능 (공백 불가), 2자 이상 10자 이하
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", user.getNickname());

        if (userService.checkEmailExists(user.getEmail())) {
            // 이메일이 이미 존재하는 경우
            throw new UserAlreadyExistsException(user.getEmail());
        } else if (userService.checkNicknameExists(user.getNickname())) {
            // 중복된 닉네임일 경우
            throw new NicknameDuplicateException(user.getNickname());
        } else if(!invalidTest) {
            // 유효성 검증 실패
            throw new InvalidValueException(user.getNickname());
        } else {
            // 조건 충족하면 회원가입시키고 로그인함
            userService.registerUser(user);
            userService.login(user);
            return ResponseEntity.ok("사용자가 성공적으로 추가되었습니다.");
        }
    }

    // 회원 정보 업데이트
    @Operation(summary = "update user", description = "회원정보를 db에 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "400", description = "경로의 회원 pk(userId)와 본문의 회원 pk가 일치하지 않음, 올바르지 않은 형식의 닉네임"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근"),
            @ApiResponse(responseCode = "403", description = "다른 유저의 정보를 변경하는 등 권한이 없는 경우"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 또는 존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 pk(userId, 자동증가 Integer 형태)", required = true),
            @Parameter(name = "nickname", description = "닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user, @PathVariable Integer userId) {

        // 요청 본문에서 가져온 닉네임
        String nickname = user.getNickname();
        // 요청 본문에서 가져온 userId
        Integer requestUserId = user.getUserId();

        // 한글, 영어, 숫자만 가능 (공백 불가), 2자 이상 10자 이하
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);


        if(!userId.equals(requestUserId)) {
            // 경로와 요청의 userId가 일치하지 않음
            throw new InconsistentException(userId, requestUserId);
        }

        if(!invalidTest) {
            // 유효한 닉네임인지 먼저 확인
            throw new InvalidValueException(user.getNickname());
        }

        // 존재하는 회원인지 확인한다.
        if(userService.checkUserIdExists(userId)) {
            // 이미 사용중인 닉네임인지 확인한다
            if(userService.checkNicknameExists(nickname)){
                throw new NicknameDuplicateException(user.getNickname());
            }
            // 이 아니면 업데이트
            userService.updateUser(user);
            return ResponseEntity.ok("사용자가 성공적으로 업데이트되었습니다.");
        } else {
            throw new UserNotExistsException(userId);
        }
    }

    // 가능한 닉네임인지 체크
    @Operation(summary = "check nickname", description = "사용가능한 닉네임인지 확인(중복, 유효한 값)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 닉네임"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 사용"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
    })
    @GetMapping("/check")
    public ResponseEntity<String> checkNickname(@RequestParam("nickname") String nickname) {
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);
        boolean isDuplicate = userService.checkNicknameExists(nickname);

        if (!invalidTest) {
            throw new InvalidValueException(nickname);
        } else if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 닉네임입니다.");
        }
    }


    @Operation(summary = "login user", description = "회원 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 완료 완료"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "email", description = "회원 이메일", required = true),
    })
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();

        if (email != null) {
            // 이메일 값이 비어있지 않은 경우에만
            UserDTO loginUser = userService.getUserByEmail(email);

            if(loginUser != null) {
                // 로그인 성공
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, null)
                );
                Object principal = authentication.getPrincipal();

                String username;

                // 유저 확인 테스트
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                return ResponseEntity.ok("로그인 성공 : " + username);
            } else {
                throw new UserNotExistsException(email);
            }
        } else {
            throw new UserNotExistsException("[null]");
        }
    }
}
