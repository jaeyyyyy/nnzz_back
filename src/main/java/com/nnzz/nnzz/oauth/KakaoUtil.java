package com.nnzz.nnzz.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoUtil {
    private final KakaoClient kakaoClient;
}
