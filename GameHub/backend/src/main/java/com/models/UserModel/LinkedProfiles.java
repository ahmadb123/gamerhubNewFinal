package com.models.UserModel;

import jakarta.persistence.Embeddable;

/*
 * shows the linked profiles of the user-
 * platforms
 * gamertags
 */

@Embeddable
public class LinkedProfiles {
    private String platform;
    private String gamertag;
    // if xbox-> save xuid and uhs in a list.
    private String xuid; 
    private String uhs;
    // if steam-> save steam id in a list.
    private String steamId;
    // if discord -> save discord id in a list.
    private String discordId;




    // empty constructor
    public LinkedProfiles() {
    }

    // Constructor
    public LinkedProfiles(String platform, String gamertag) {
        this.platform = platform;
        this.gamertag = gamertag;
    }

    // Getters and Setters

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public String getUhs() {
        return uhs;
    }

    public void setUhs(String uhs) {
        this.uhs = uhs;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
}
