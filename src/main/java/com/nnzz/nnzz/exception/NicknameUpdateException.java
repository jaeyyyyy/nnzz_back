package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class NicknameUpdateException extends IllegalArgumentException{
    private String nickname;

    public NicknameUpdateException(String nickname) {
        super("닉네임 : " + nickname + "은 30일에 한 번만 수정 가능 합니다.");
        this.nickname = nickname;
    }
}
