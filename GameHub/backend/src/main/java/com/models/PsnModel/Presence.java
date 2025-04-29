package com.models.PsnModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Presence info (online/offline status, platform, active title).
 */
public class Presence {
    @JsonProperty("onlineStatus")
    private String onlineStatus;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("titleId")
    private String titleId;

    // getters & setters
    public String getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(String onlineStatus) { this.onlineStatus = onlineStatus; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }
}
