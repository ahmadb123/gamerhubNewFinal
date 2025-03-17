package com.models.XboxModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MobileLoginResponse {
    private String authUrl;
    private String codeVerifier; // Critical for mobile flow

    // empty constructor

    public MobileLoginResponse() {
    }
    
    public MobileLoginResponse(String authUrl, String codeVerifier) {
        this.authUrl = authUrl;
        this.codeVerifier = codeVerifier;
    }
    @JsonProperty("url")
    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }
}