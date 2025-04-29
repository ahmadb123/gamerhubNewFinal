package com.models.PsnModel;

// handles psn friends list

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Top‐level container for a PSN friends response.
 */
public class PsnFriendsList {
    @JsonProperty("friends")
    private List<PsnFriend> friends;

    public List<PsnFriend> getFriends() {
        return friends;
    }

    public void setFriends(List<PsnFriend> friends) {
        this.friends = friends;
    }
}
