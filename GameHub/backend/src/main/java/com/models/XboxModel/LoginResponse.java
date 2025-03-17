package com.models.XboxModel;

// import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private String redirectUrl;


    // empty constructor
    public LoginResponse() {
    }
    // Constructor
    public LoginResponse(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    // Getter and Setter
    // @JsonProperty("url")
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
