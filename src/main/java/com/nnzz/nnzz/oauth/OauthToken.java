package com.nnzz.nnzz.oauth;

public interface OauthToken {
    String tokenType();
    String accessToken();
    int expiresIn();
    String refreshToken();
    String scope();
    int refreshTokenExpiresIn();
}
