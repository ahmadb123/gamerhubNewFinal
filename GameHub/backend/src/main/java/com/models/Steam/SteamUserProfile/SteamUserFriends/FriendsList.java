package com.models.Steam.SteamUserProfile.SteamUserFriends;

import java.util.List;

/*
 * This class is used to get friends list from Steam API
 */
public class FriendsList{
    List<Friends> friends;
    
    // Getters and Setters
    public List<Friends> getFriends() {
        return friends;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }
}