package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SteamLoginRequest {
    @JsonProperty("SteamId")
    private String steamId;

    @JsonProperty("CreateAccount")
    private boolean createAccount;

    // Getters and Setters
    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public boolean isCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(boolean createAccount) {
        this.createAccount = createAccount;
    }

}
