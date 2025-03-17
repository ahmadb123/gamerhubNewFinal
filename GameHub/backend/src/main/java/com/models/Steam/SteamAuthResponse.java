package com.models.Steam;

public class SteamAuthResponse {
    public String success;
    public String steamID;
    public String error;

    public SteamAuthResponse(String success, String steamID, String error) {
        this.success = success;
        this.steamID = steamID;
        this.error = error;
    }

    // empty constructor
    public SteamAuthResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSteamID() {
        return steamID;
    }

    public void setSteamID(String steamID) {
        this.steamID = steamID;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
