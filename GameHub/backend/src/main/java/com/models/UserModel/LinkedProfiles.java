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

}
