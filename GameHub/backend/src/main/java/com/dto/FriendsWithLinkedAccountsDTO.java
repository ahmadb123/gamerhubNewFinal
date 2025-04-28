package com.dto;

import java.util.List;

import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.Steam.SteamUserProfile.UserInfo;
import com.models.XboxModel.XboxProfile;

public class FriendsWithLinkedAccountsDTO {
    private Long userId;
    private String username;
    private String email;            // if you want email or other fields
    private List<XboxProfile> xboxProfiles;
    private List<SteamProfile> steamProfiles; // Assuming you have a SteamProfile class
    private List<XboxRecentGameDTO> xboxRecentGames; // New property for Xbox recent games
    private UserInfo steamRecentGames; // Modified property type for Steam recent games
    public FriendsWithLinkedAccountsDTO() {}

    // Getters and setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<XboxProfile> getXboxProfiles() {
        return xboxProfiles;
    }
    public void setXboxProfiles(List<XboxProfile> xboxProfiles) {
        this.xboxProfiles = xboxProfiles;
    }

    public List<SteamProfile> getSteamProfiles() {
        return steamProfiles;
    }

    public void setSteamProfiles(List<SteamProfile> steamProfiles) {
        this.steamProfiles = steamProfiles;
    }

    // New getter and setter for xboxRecentGames
    public List<XboxRecentGameDTO> getXboxRecentGames() {
        return xboxRecentGames;
    }
    public void setXboxRecentGames(List<XboxRecentGameDTO> xboxRecentGames) {
        this.xboxRecentGames = xboxRecentGames;
    }

    // New getter and setter for steamRecentGames
    public UserInfo getSteamRecentGames() {
        return steamRecentGames;
    }
    public void setSteamRecentGames(UserInfo steamRecentGames) {
        this.steamRecentGames = steamRecentGames;
    }
}
