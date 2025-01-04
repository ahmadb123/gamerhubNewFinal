package com.models.NewsModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @JsonProperty("xuid")
    private String xuid;

    @JsonProperty("isFavorite")
    private Boolean favorite;

    @JsonProperty("isFollowingCaller")
    private Boolean followingCaller;

    @JsonProperty("isFollowedByCaller")
    private Boolean followedByCaller;

    @JsonProperty("isIdentityShared")
    private Boolean identityShared;

    @JsonProperty("addedDateTimeUtc")
    private String addedDateTimeUtc;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("realName")
    private String realName;

    @JsonProperty("gamerScore")
    private String gamerScore;

    @JsonProperty("presenceText")
    private String presenceText;

    @JsonProperty("displayPicRaw")
    private String displayPicRaw;

    @JsonProperty("modernGamertag")
    private String modernGamertag;

    @JsonProperty("gamertag")
    private String gamertag;

    @JsonProperty("presenceState")
    private String presenceState;

    @JsonProperty("colorTheme")
    private String colorTheme;

    @JsonProperty("preferredPlatforms")
    private String[] preferredPlatforms;

    // Default constructor
    public Person() {}

    // Getters and Setters
    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getFollowingCaller() {
        return followingCaller;
    }

    public void setFollowingCaller(Boolean followingCaller) {
        this.followingCaller = followingCaller;
    }

    public Boolean getFollowedByCaller() {
        return followedByCaller;
    }

    public void setFollowedByCaller(Boolean followedByCaller) {
        this.followedByCaller = followedByCaller;
    }

    public Boolean getIdentityShared() {
        return identityShared;
    }

    public void setIdentityShared(Boolean identityShared) {
        this.identityShared = identityShared;
    }

    public String getAddedDateTimeUtc() {
        return addedDateTimeUtc;
    }

    public void setAddedDateTimeUtc(String addedDateTimeUtc) {
        this.addedDateTimeUtc = addedDateTimeUtc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGamerScore() {
        return gamerScore;
    }

    public void setGamerScore(String gamerScore) {
        this.gamerScore = gamerScore;
    }

    public String getPresenceText() {
        return presenceText;
    }

    public void setPresenceText(String presenceText) {
        this.presenceText = presenceText;
    }

    public String getDisplayPicRaw() {
        return displayPicRaw;
    }

    public void setDisplayPicRaw(String displayPicRaw) {
        this.displayPicRaw = displayPicRaw;
    }

    public String getModernGamertag() {
        return modernGamertag;
    }

    public void setModernGamertag(String modernGamertag) {
        this.modernGamertag = modernGamertag;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getPresenceState() {
        return presenceState;
    }

    public void setPresenceState(String presenceState) {
        this.presenceState = presenceState;
    }

    public String getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
    }

    public String[] getPreferredPlatforms() {
        return preferredPlatforms;
    }

    public void setPreferredPlatforms(String[] preferredPlatforms) {
        this.preferredPlatforms = preferredPlatforms;
    }
}
