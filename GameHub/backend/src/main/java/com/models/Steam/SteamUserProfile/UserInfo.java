package com.models.Steam.SteamUserProfile;

import java.util.List;

/*
 * This class is used to get player summaries from Steam API
 * Endpoint - https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/
 *      ?key=79292D739A20F25E05BC6F85697FCB2C&steamids=76561199833183940
 */
public class UserInfo {
    private List<Players> players;

    // empty constructor
    public UserInfo() {
    }

    // Constructor

    public UserInfo(List<Players> players) {
        this.players = players;
    }
    
    public List<Players> getPlayers() {
        return players;
    }

    public void setPlayers(List<Players> players) {
        this.players = players;
    }



}
