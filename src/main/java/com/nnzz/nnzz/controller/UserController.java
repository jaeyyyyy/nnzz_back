package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.jwt.JwtToken;
import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.*;
import com.nnzz.nnzz.exception.*;
import com.nnzz.nnzz.service.AuthService;
import com.nnzz.nnzz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.regex.Pattern;

@Tag(name="users", description = "냠냠쩝쩝 회원 추가 설정 및 회원 관리")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    // 회원가입, 회원가입 전에 닉네임 사용가능 여부 체크, 로그인 제외 전부 토큰 필요

    /**
     * 회원가입
     * 성공하면 db 저장 후, 바로 로그인 과정 진행
     * @param user
     * @return
     */
    @Operation(summary = "register user", description = "<strong>\uD83D\uDCA1회원 정보를 db에 저장</strong>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이메일이 null인 경우, 이미 회원가입한 유저, 올바르지 않은 형식의 닉네임, 이미 사용중인 닉네임",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "email", description = "카카오 oauth에서 받아온 유저의 이메일", required = true),
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지. 1,2,3... 같은 int 값을 저장하고 그 값에 해당하는 이미지 주소는 따로 링크시키는 방법을 생각 중", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PostMapping("/join")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserDTO.JoinRequest user) {

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
            // 조건 충족하면 회원가입시키고 로그인 -> 토큰과 함께 유저 정보를 반환
            UserDTO saveUser = userService.registerUser(user);
            JwtToken jwtToken = authService.signIn(saveUser.getEmail());
            UserResponse response = userService.returnUserResponse(jwtToken, saveUser);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 로그인
     * @param loginRequest 로 email을 받으면 토큰값과 유저 정보 리턴
     * @return
     */
    @Operation(summary = "login user", description = "<strong>\uD83D\uDCA1회원 로그인</strong>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 값, 가입되지 않은 유저",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "email", description = "회원 이메일", required = true),
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        // String encryptedEmail = seed.encrypt(loginRequest.getEmail());

        // 이메일 값이 비어있지 않은 경우에만
        if (email != null) {
            // 해당 이메일을 가진 유저가 있는지 확인
            UserDTO loginUser = userService.getOptionalUserByEmail(email).orElse(null);
            System.out.println("loginUser : " + loginUser);

            if(loginUser != null) {
                // 토큰 생성
                JwtToken jwtToken = authService.signIn(email);
                // 토큰과 함께 유저 정보 반환
                UserResponse response = userService.returnUserResponse(jwtToken, loginUser);
                return ResponseEntity.ok(response);

//                // 로그인 성공
//                Authentication auth = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(email, null)
//                );
//
//                // 인증 정보를 SecurityContext에 저장
//                SecurityContextHolder.getContext().setAuthentication(auth);
//
//                // 로그인 잘 되는지 테스트
//                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
//                String authEmail = currentAuth.getName();
//
//                Object principal = auth.getPrincipal();
//
//                // dto 넘김
//                if(principal instanceof UserInfoDetails) {
//                    UserInfoDetails userInfoDetails = (UserInfoDetails) principal;
//
//                    LoginUserDTO userInfo = LoginUserDTO.builder()
//                            // .id(testAuthUserId)
//                            .id(userInfoDetails.getUserId())
//                            .nickname(userInfoDetails.getUserNickname())
//                            .email(seed.decrypt(authEmail))
//                            //.email(seed.decrypt(userInfoDetails.getUsername()))
//                            .profileImage(userInfoDetails.getProfileImage())
//                            .gender(userInfoDetails.getGender())
//                            .age(userInfoDetails.getAgeRange())
//                            .build();
//
//                    return ResponseEntity.ok(userInfo);
//                } else {
//                    throw new UserNotExistsException(email);
//                }
            } else {
                throw new UserNotExistsException(email);
            }
        } else {
            throw new UserNotExistsException("[null]");
        }
    }

    @Operation(summary = "logout user", description = "<strong>\uD83D\uDCA1회원 로그아웃</strong><br>블랙리스트에 토큰을 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 값, 가입되지 않은 유저",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "email", description = "회원 이메일", required = true),
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseDetail> logout(@RequestHeader("Authorization") String token, HttpServletRequest httpServletRequest) {
        // 현재 인증된 사용자 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()) {
            // 로그아웃 처리
            userService.logout(token);
            ResponseDetail responseDetail = userService.returnLogoutResponse(httpServletRequest.getRequestURI());
            return ResponseEntity.ok(responseDetail);
        } else {
            throw new UnauthorizedException("로그인 된 사용자가 아닙니다.");
        }
    }


    /**
     * 회원 정보 수정
     * 헤더에 토큰값이 없는 경우 에러
     * @param updateUser
     * @return
     */
    @Operation(summary = "update user", description = "<strong>\uD83D\uDCA1회원정보를 db에 업데이트</strong><br>변경되지 않는 값도 전부(닉네임,프로필이미지,성별,나이대)를 받음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 닉네임, 이미 존재하는 닉네임",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지", required = true),
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true),
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUser, HttpServletRequest httpServletRequest) {
        // Authentication에서 가져온 유저 정보

        int authUserId = SecurityUtils.getUserId();
        UserDTO authUser = userService.getUserByUserId(authUserId);

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
        ResponseDetail response = userService.returnUpdateUserResponse(httpServletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "update user nickname", description = "<strong>\uD83D\uDCA1회원의 닉네임을 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 닉네임, 이미 존재하는 닉네임"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근")
    })
    @Parameters({
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true)
    })
    @PatchMapping("/nickname")
    public ResponseEntity<ResponseDetail> updateUserNickname(@RequestBody UpdateUserDTO.NicknameRequest request, HttpServletRequest httpServletRequest) {
        int authUserId = SecurityUtils.getUserId();
        // 요청 본문에서 가져온 닉네임
        String nickname = request.getNickname();

        // 이미 사용중인 닉네임인지 확인한다
        if(userService.checkNicknameExists(nickname, authUserId)){
            throw new NicknameDuplicateException(nickname);
        }

        userService.updateUserNickname(nickname, authUserId);
        ResponseDetail response = userService.returnUpdateUserResponse(httpServletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "update user age and gender", description = "<strong>\uD83D\uDCA1회원의 나이대와 성별을 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 나이대와 성별"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근")
    })
    @Parameters({
            @Parameter(name = "gender", description = "냠냠쩝쩝에서 설정한 유저의 성별", required = true),
            @Parameter(name = "ageRange", description = "냠냠쩝쩝에서 설정한 유저의 나이대", required = true)
    })
    @PatchMapping("/age-and-gender")
    public ResponseEntity<ResponseDetail> updateUserAgeGender(@RequestBody UpdateUserDTO.AgeAndGenderRequest request, HttpServletRequest httpServletRequest) {
        int authUserId = SecurityUtils.getUserId();
        // 요청 본문에서 가져온 나이대와 성별
        String ageRange = request.getAgeRange();
        String gender = request.getGender();

        userService.updateUserAgeRangeAndGender(gender, ageRange, authUserId);
        ResponseDetail response = userService.returnUpdateUserResponse(httpServletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "update user profile image", description = "<strong>\uD83D\uDCA1회원의 프로필 이미지를 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 프로필 이미지"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 수정 접근")
    })
    @Parameters({
            @Parameter(name = "profileImage", description = "냠냠쩝쩝에서 설정한 유저의 프로필 이미지", required = true)
    })
    @PatchMapping("/profile-image")
    public ResponseEntity<ResponseDetail> updateUserProfileImage(@RequestBody UpdateUserDTO.ProfileImageRequest request, HttpServletRequest httpServletRequest) {
        int authUserId = SecurityUtils.getUserId();
        // 요청 본문에서 가져온 프로필 이미지
        String profileImage = request.getProfileImage();

        userService.updateUserProfileImage(profileImage, authUserId);
        ResponseDetail response = userService.returnUpdateUserResponse(httpServletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }


    /**
     * 회원 탈퇴
     * @return
     */
    @Operation(summary = "delete user", description = "<strong>\uD83D\uDCA1회원 탈퇴</strong>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 완료 완료"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 탈퇴 접근",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "email", description = "회원 이메일", required = true),
    })
    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<ResponseDetail> deleteUser(HttpServletRequest httpServletRequest) {
        Integer userId = SecurityUtils.getUserId();

        try {
            userService.deleteUser(userId);
            ResponseDetail responseDetail = userService.returnDeleteUserResponse(httpServletRequest.getRequestURI());
            return ResponseEntity.ok(responseDetail);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 오류입니다.");
        }
    }


    /**
     * 가능한 닉네임인지만 체크
     * @param nickname
     * @return
     */
    @Operation(summary = "check nickname", description = "<strong>\uD83D\uDCA1사용가능한 닉네임인지 확인(중복, 유효한 값)</strong><br>회원가입 전에 사용가능한지 체크합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "올바른 닉네임 형식이 아님, 이미 사용중인 닉네임",
                    content = @Content(schema = @Schema(implementation = ResponseDetail.class)))
    })
    @Parameters({
            @Parameter(name = "nickname", description = "냠냠쩝쩝에서 설정한 유저의 닉네임", required = true),
    })
    @GetMapping("/check")
    public ResponseEntity<ResponseDetail> checkNickname(@RequestParam("nickname") String nickname, HttpServletRequest httpServletRequest) {
        boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);
        boolean isDuplicate = userService.checkNicknameExists(nickname, null);

        if (!invalidTest) {
            throw new InvalidValueException("닉네임 : " + nickname + " 올바른 닉네임 형식이 아닙니다.");
        } else if (isDuplicate) {
            throw new InvalidValueException("닉네임 : " + nickname + " 이미 사용중인 닉네임입니다.");
        } else {
            ResponseDetail responseDetail = userService.returnCheckNicknameResponse(httpServletRequest.getRequestURI());
            return ResponseEntity.ok(responseDetail);
        }
    }

//    @PostMapping("/test")
//    public ResponseEntity<?> test(){
//        String email = SecurityUtils.getUserEmail();
//        UserDTO user = userService.getUserByEmail(email);
//        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
//                .id(user.getUserId())
//                .nickname(user.getNickname())
//                .email(user.getEmail())
//                .profileImage(user.getProfileImage())
//                .gender(user.getGender())
//                .age(user.getAgeRange())
//                .build();
//        return ResponseEntity.ok(loginUserDTO);
//    }
//
//    @PostMapping("/test1")
//    public ResponseEntity<?> test1(){
//        return ResponseEntity.ok("테스트");
//    }
}
