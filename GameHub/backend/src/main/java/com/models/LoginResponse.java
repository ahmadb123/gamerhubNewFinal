package com.models;

public class LoginResponse {
    private String redirectUrl;

    // Constructor
    public LoginResponse(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    // Getter and Setter
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
