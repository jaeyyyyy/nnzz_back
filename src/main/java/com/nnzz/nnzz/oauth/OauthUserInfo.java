package com.nnzz.nnzz.oauth;

public interface OauthUserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
