package com.models.XboxModel;

public class MobileLoginResponse {
    private String authUrl;
    private String codeVerifier; // Critical for mobile flow

    public MobileLoginResponse(String authUrl, String codeVerifier) {
        this.authUrl = authUrl;
        this.codeVerifier = codeVerifier;
    }
    
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