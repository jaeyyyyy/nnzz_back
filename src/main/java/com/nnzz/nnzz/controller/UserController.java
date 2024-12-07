package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.seed.Seed;
import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.exception.*;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Tag(name="users", description = "냠냠쩝쩝 회원 추가 설정 및 회원 관리")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final Seed seed;

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
        } else if (userService.checkNicknameExists(user.getNickname(), null)) {
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
        String encryptedEmail = seed.encrypt(loginRequest.getEmail());

        // 이메일 값이 비어있지 않은 경우에만
        if (email != null) {
            // 해당 이메일을 가진 유저가 있는지 확인
            UserDTO loginUser = userService.getOptionalUserByEmail(encryptedEmail).orElse(null);

            if(loginUser != null) {
                // 로그인 성공
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, null)
                );

                // 인증 정보를 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(auth);

                Object principal = auth.getPrincipal();

                // dto 넘김
                if(principal instanceof UserInfoDetails) {
                    UserInfoDetails userInfoDetails = (UserInfoDetails) principal;

                    LoginUserDTO userInfo = LoginUserDTO.builder()
                            .id(userInfoDetails.getUserId())
                            .nickname(userInfoDetails.getUserNickname())
                            .email(seed.decrypt(userInfoDetails.getUsername()))
                            .profileImage(userInfoDetails.getProfileImage())
                            .gender(userInfoDetails.getGender())
                            .age(userInfoDetails.getAgeRange())
                            .build();

                    return ResponseEntity.ok(userInfo);
                } else {
                    throw new UserNotExistsException(email);
                }
            } else {
                throw new UserNotExistsException(email);
            }
        } else {
            throw new UserNotExistsException("[null]");
        }
    }


    // 회원 정보 업데이트
    @Operation(summary = "update user", description = "회원정보를 db에 업데이트, (2024-12-01) 로그인 된 유저 정보를 받아오는 것으로 수정 / 수정할 값(닉네임,프로필이미지,성별,나이대)만 보내주시면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근"),
            @ApiResponse(responseCode = "404", description = "적절하지 않은 닉네임 값"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "nickname", description = "닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUser) {

        // Authentication에서 가져온 유저 정보
        UserDTO authUser = SecurityUtils.getCurrentUser();
        Integer authUserId;

        if(authUser == null) {
            throw new UnauthorizedException("인증되지 않은 유저입니다.");
        } else {
            // AuthUser에서 가져온 userId
            authUserId = authUser.getUserId();
        }

        // 요청 본문에서 가져온 닉네임
        String nickname = updateUser.getNickname();

        // 한글, 영어, 숫자만 가능 (공백 불가), 2자 이상 10자 이하
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);
        if(!invalidTest) {
            // 유효한 닉네임인지 먼저 확인
            throw new InvalidValueException(nickname);
        }

        // 이미 사용중인 닉네임인지 확인한다
        if(userService.checkNicknameExists(nickname, authUserId)){
            throw new NicknameDuplicateException(nickname);
        }


        // UserDTO 객체를 빌더를 사용하여 생성하여 업데이트
        UserDTO userToUpdate = UserDTO.builder()
                .userId(authUserId) // 인증된 사용자 ID
                .email(authUser.getEmail()) // 이메일은 수정 불가하므로 기존 이메일 사용
                .nickname(updateUser.getNickname())
                .profileImage(updateUser.getProfileImage())
                .gender(updateUser.getGender())
                .ageRange(updateUser.getAgeRange())
                .lastNicknameChangeDate(authUser.getLastNicknameChangeDate())
                .build();

        // 이 아니면 업데이트

        userService.updateUser(userToUpdate);
        return ResponseEntity.ok("사용자가 성공적으로 업데이트되었습니다.");

    }



    @Operation(summary = "delete user", description = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 완료 완료"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "email", description = "회원 이메일", required = true),
    })
    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Integer userId = SecurityUtils.getUserId();

        if(userId != null) {
            try {
                userService.deleteUser(userId);
                return ResponseEntity.ok("회원 탈퇴 성공");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("알수 없는 오류입니다.");
            }
        } else {
            throw new UserNotExistsException("[null]");
        }
    }

    // 유저의 정보를 가져오기
    @GetMapping
    public ResponseEntity<?> getUsers() {
        UserDTO authUser = SecurityUtils.getCurrentUser();

        return ResponseEntity.ok(authUser); // 성공적으로 유저 정보를 반환
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
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);
        boolean isDuplicate = userService.checkNicknameExists(nickname, null);

        if (!invalidTest) {
            throw new InvalidValueException(nickname);
        } else if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 닉네임입니다.");
        }
    }
}
