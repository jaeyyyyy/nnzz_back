package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.ResponseDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResponseDetailService {
    private ResponseDetail createResponseDetail(String requestURI, String detail, String message) {
        return new ResponseDetail(
                "about:blank",
                "OK",
                200,
                detail,
                requestURI,
                LocalDateTime.now().toString(),
                message
        );
    }

    // 유저 업데이트 성공시 응답
    public ResponseDetail returnUpdateUserResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "사용자가 성공적으로 업데이트되었습니다.",
                "업데이트 완료"
        );
    }

    // 유저 로그아웃 성공시 응답
    public ResponseDetail returnLogoutResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "사용자가 성공적으로 로그아웃 하였습니다.",
                "로그아웃 완료"
        );
    }

    // 유저 탈퇴 성공시 응답
    public ResponseDetail returnDeleteUserResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "사용자가 성공적으로 탈퇴하였습니다.",
                "탈퇴 완료"
        );
    }

    // 닉네임 체크 성공시 응답
    public ResponseDetail returnCheckNicknameResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "사용가능한 닉네임입니다.",
                "사용가능한 닉네임입니다."
        );
    }

    // 오픈 신청 성공시 응답
    public ResponseDetail returnOpenLocationResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "오픈 신청이 완료되었습니다.",
                "오픈 신청 완료"
        );
    }

    // 위치 저장 성공시 응답
    public ResponseDetail returnSaveLocationResponse(String requestURI) {
        return createResponseDetail(
                requestURI,
                "위치 저장이 완료되었습니다.",
                "위치 저장 완료"
        );
    }
}
