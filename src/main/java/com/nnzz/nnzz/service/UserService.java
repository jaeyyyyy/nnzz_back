package com.nnzz.nnzz.service;

import com.nnzz.nnzz.config.jwt.JwtToken;
import com.nnzz.nnzz.dto.BlacklistToken;
import com.nnzz.nnzz.dto.LoginUserDTO;
import com.nnzz.nnzz.dto.ProblemDetailResponse;
import com.nnzz.nnzz.dto.UserDTO;
import com.nnzz.nnzz.exception.EmailIsNullExcepion;
import com.nnzz.nnzz.exception.InvalidValueException;
import com.nnzz.nnzz.exception.NicknameUpdateException;
import com.nnzz.nnzz.exception.UserNotExistsException;
import com.nnzz.nnzz.repository.BlacklistTokenMapper;
import com.nnzz.nnzz.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;
    private final BlacklistTokenMapper blacklistTokenMapper;

    // 성공시 응답
    public ProblemDetailResponse returnUpdateUserResponse() {
        return new ProblemDetailResponse(
                "about:blank",
                "OK",
                200,
                "사용자가 성공적으로 업데이트되었습니다.",
                LocalDateTime.now().toString(),
                "업데이트 완료"
        );
    }

    public Map<String, Object> returnUserResponse(JwtToken jwtToken, UserDTO loginUser) {
        Map<String, Object> response = new HashMap<>();

        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                .id(loginUser.getUserId())
                .nickname(loginUser.getNickname())
                .email(loginUser.getEmail())
                .profileImage(loginUser.getProfileImage())
                .gender(loginUser.getGender())
                .age(loginUser.getAgeRange())
                .build();

        response.put("token", jwtToken);
        response.put("user", loginUserDTO);

        return response;
    }



    @Transactional
    public boolean logout(String token) {
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(blacklistTokenMapper.existsByToken(token)) {
            throw new AccountExpiredException("이미 로그아웃한 토큰입니다.");
        }

        // 토큰 만료 시간 설정 (하루 뒤)
        LocalDateTime expiry = LocalDateTime.now().plusDays(1);
        // 블랙리스트에 추가
        BlacklistToken blacklistToken = new BlacklistToken(token, expiry);
        blacklistTokenMapper.insertBlacklistToken(blacklistToken);
        return true;
    }


    // 유저 찾기
    public UserDTO getUserByUserId(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    // 유저 찾기
    public UserDTO getUserByEmail(String email) {
        Optional<UserDTO> userByEmail = getOptionalUserByEmail(email);
        return userByEmail.orElse(null);
    }

    // 회원정보 수정
    @Transactional
    public void updateUser(UserDTO user){
        UserDTO existingUser = getUserByUserId(user.getUserId());

        // 기존 닉네임을 전달
        UserDTO oldUser = UserDTO.builder()
                .userId(existingUser.getUserId())
                .email(existingUser.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .lastNicknameChangeDate(existingUser.getLastNicknameChangeDate())
                .build();


        // 닉네임이 변경되었는지 확인
        if(!existingUser.getNickname().equals(user.getNickname())){
            LocalDateTime lastChangeDate = existingUser.getLastNicknameChangeDate();
            LocalDateTime now = LocalDateTime.now();

            // 닉네임 변경 가능 여부 체크
            if(lastChangeDate != null){
                long daysBetween = ChronoUnit.DAYS.between(lastChangeDate, now);
                if(daysBetween < 30){
                    throw new NicknameUpdateException(user.getNickname());
                }
            }
        }
        userMapper.updateUser(oldUser);
    }

    // 회원정보 수정(프로필 이미지 수정)
    @Transactional
    public void updateUserProfileImage(String profileImage, Integer userId) {
        if(profileImage != null) {
            // 프로필 이미지 업데이트
            userMapper.updateUserProfileImage(profileImage, userId);
        }
    }

    // 회원정보 수정(닉네임 변경)
    @Transactional
    public void updateUserNickname(String nickname, Integer userId) {
        if(nickname != null) {
            // 한글, 영어, 숫자만 가능 (공백 불가), 2자 이상 10자 이하
            boolean invalidTest = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}$", nickname);
            if(!invalidTest) {
                // 유효한 닉네임인지 먼저 확인
                throw new InvalidValueException(nickname);
            }

            UserDTO existingUser = getUserByUserId(userId);
            // 닉네임이 변경되었는지 확인
            if(!existingUser.getNickname().equals(nickname)){
                LocalDateTime lastChangeDate = existingUser.getLastNicknameChangeDate();
                LocalDateTime now = LocalDateTime.now();

                // 닉네임 변경 가능 여부 체크
                if(lastChangeDate != null){
                    long daysBetween = ChronoUnit.DAYS.between(lastChangeDate, now);
                    if(daysBetween < 30){
                        throw new NicknameUpdateException(nickname);
                    }
                }
            }

            userMapper.updateUserNickname(nickname, userId);
        }
    }

    // 회원정보 수정(성별/나이대 변경)
    @Transactional
    public void updateUserAgeRangeAndGender(String gender, String ageRange, Integer userId) {
        if(gender != null && ageRange != null) {
            userMapper.updateUserAgeAndGender(gender, ageRange, userId);
        }
    }

    // 회원가입 처리
    @Transactional
    public UserDTO registerUser(UserDTO user) {
        if(user.getEmail() == null) {
            throw new EmailIsNullExcepion("회원가입 시 이메일은 필수 항목입니다.");
        }


        UserDTO savedUser = UserDTO.builder()
                .email(user.getEmail())
                //.email(seed.encrypt(user.getEmail()))
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .build();
        userMapper.createUser(savedUser);
        return savedUser;
    }

    // 이메일 중복 찾기
    public boolean checkEmailExists(String email) {
        // return userMapper.existsUserByEmail(seed.encrypt(email));
        return userMapper.existsUserByEmail(email);
    }

    public Optional<UserDTO> getOptionalUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public Optional<UserDTO> getOptionalUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }


    // 닉네임 중복 찾기
    public boolean checkNicknameExists(String nickname, Integer userId) {
        return userMapper.existsUserByNickname(nickname, userId);
    }

    // 실제 있는 userId인지 확인
    public boolean checkUserIdExists(int userId) {
        return userMapper.existsUserByUserId(userId);
    }

    // 로그인
    public UserDTO login(UserDTO user) {
        Optional<UserDTO> userByEmail = getOptionalUserByEmail(user.getEmail());
        // Optional<UserDTO> userByEmail = getOptionalUserByEmail(seed.encrypt(user.getEmail()));
        if (userByEmail.isPresent()) {
            // 이메일로 조회했을 때, 결과가 있다면
            return userByEmail.get();
        } else {
            // 조회결과가 없으면 리턴하지 않음
            return null;
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Integer userId) {
        // 사용자 찾기
        getOptionalUserByUserId(userId)
                .orElseThrow(() -> new UserNotExistsException(userId));
        // 사용자 삭제
        userMapper.deleteUserByUserId(userId);
    }
}
