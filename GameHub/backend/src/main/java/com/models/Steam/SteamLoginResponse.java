package com.models.Steam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SteamLoginResponse {
    @JsonProperty("redirectUrl")
    public String redirectURL;
    public String error;


    // empty constructor
    public SteamLoginResponse() {
    }
    public SteamLoginResponse(String redirectURL, String error) {
        this.redirectURL = redirectURL;
        this.error = error;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
