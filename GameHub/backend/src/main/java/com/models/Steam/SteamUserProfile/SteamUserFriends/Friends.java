package com.models.Steam.SteamUserProfile.SteamUserFriends;

public class Friends {
    private String steamId;
    private String relationship;
    private Long friendSince;
    // these fields dont belong from the steam api-> 
    private String friendName;
    private String friendAvatar;
    private String friendProfileUrl;

    // Getters and Setters

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Long getFriendSince() {
        return friendSince;
    }

    public void setFriendSince(Long friendSince) {
        this.friendSince = friendSince;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

    public String getFriendProfileUrl() {
        return friendProfileUrl;
    }

    public void setFriendProfileUrl(String friendProfileUrl) {
        this.friendProfileUrl = friendProfileUrl;
    }
}
