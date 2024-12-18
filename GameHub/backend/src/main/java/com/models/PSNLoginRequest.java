package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PSNLoginRequest {
    @JsonProperty("AuthCode")
    private String authCode;

    @JsonProperty("RedirectUri")
    private String redirectUri;

    @JsonProperty("CreateAccount")
    private boolean createAccount;

    // Getters and Setters
    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }
}
