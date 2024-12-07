package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class InvalidNicknameException extends IllegalArgumentException {
    private final String nickname;

    public InvalidNicknameException(String nickname) {
        super("Invalid nickname: " + nickname + "닉네임은 2자 이상 10자 이하여야 하며, 띄어쓰기 없이 한글, 영문, 숫자만 입력 가능합니다.");
        this.nickname = nickname;
    }
}
