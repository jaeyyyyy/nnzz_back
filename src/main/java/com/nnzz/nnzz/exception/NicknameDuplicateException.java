package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class NicknameDuplicateException extends IllegalArgumentException {
    private final String nickname;

    public NicknameDuplicateException(String nickname) {
        super("닉네임 : " + nickname + "은 이미 사용 중인 닉네임입니다.");
        this.nickname = nickname;
    }
}
