package com.models.PsnModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A single entry in the PSN “recently played” list.
 */
public class PsnRecentGame {
    @JsonProperty("titleId")
    private String titleId;

    @JsonProperty("titleName")
    private String titleName;

    @JsonProperty("lastPlayedDate")
    private ZonedDateTime lastPlayedDate;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("iconUrls")
    private List<AvatarUrl> iconUrls;

    // getters & setters

    public String getTitleId() {
        return titleId;
    }
    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }
    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public ZonedDateTime getLastPlayedDate() {
        return lastPlayedDate;
    }
    public void setLastPlayedDate(ZonedDateTime lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<AvatarUrl> getIconUrls() {
        return iconUrls;
    }
    public void setIconUrls(List<AvatarUrl> iconUrls) {
        this.iconUrls = iconUrls;
    }
}
