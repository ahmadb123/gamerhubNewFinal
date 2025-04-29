package com.models.PsnModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * A single PSN friend entry.
 */
public class PsnFriend {
    @JsonProperty("onlineId")
    private String onlineId;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("avatarUrls")
    private List<AvatarUrl> avatarUrls;

    @JsonProperty("isFollowingMe")
    private boolean isFollowingMe;

    @JsonProperty("isFollowRequestAccepted")
    private boolean isFollowRequestAccepted;

    @JsonProperty("presence")
    private Presence presence;

    // getters & setters
    public String getOnlineId() { return onlineId; }
    public void setOnlineId(String onlineId) { this.onlineId = onlineId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public List<AvatarUrl> getAvatarUrls() { return avatarUrls; }
    public void setAvatarUrls(List<AvatarUrl> avatarUrls) { this.avatarUrls = avatarUrls; }

    public boolean isFollowingMe() { return isFollowingMe; }
    public void setFollowingMe(boolean followingMe) { isFollowingMe = followingMe; }

    public boolean isFollowRequestAccepted() { return isFollowRequestAccepted; }
    public void setFollowRequestAccepted(boolean followRequestAccepted) {
        isFollowRequestAccepted = followRequestAccepted;
    }

    public Presence getPresence() { return presence; }
    public void setPresence(Presence presence) { this.presence = presence; }
}
