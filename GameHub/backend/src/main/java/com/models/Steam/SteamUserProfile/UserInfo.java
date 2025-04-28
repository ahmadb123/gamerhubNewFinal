package com.models.Steam.SteamUserProfile;

import java.util.List;

/*
 * This class is used to get player summaries from Steam API
 * Endpoint - https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/
 *      ?key=79292D739A20F25E05BC6F85697FCB2C&steamids=76561199833183940
 * Endpint for games played and owned - https://api.steampowered.com/
 * IPlayerService/GetOwnedGames/v0001/
 * ?key=79292D739A20F25E05BC6F85697FCB2C&
 * steamid=76561199833183940&include_appinfo=1&include_played_free_games=1
 */
public class UserInfo {
    private List<SteamProfile> players;
    // from the other api get game_count - 
    private int game_count;
    private List<UserOwnedAndPlayedGames> owenedAndPlayedGames;

    // empty constructor
    public UserInfo() {
    }

    // Constructor

    public UserInfo(List<SteamProfile> players) {
        this.players = players;
    }
    
    public List<SteamProfile> getPlayers() {
        return players;
    }

    public void setPlayers(List<SteamProfile> players) {
        this.players = players;
    }

    public int getGame_count() {
        return game_count;
    }

    public void setGame_count(int game_count) {
        this.game_count = game_count;
    }

    public List<UserOwnedAndPlayedGames> getOwenedAndPlayedGames() {
        return owenedAndPlayedGames;
    }

    public void setOwenedAndPlayedGames(List<UserOwnedAndPlayedGames> owenedAndPlayedGames) {
        this.owenedAndPlayedGames = owenedAndPlayedGames;
    }

    @Override
    public String toString() {
        // Customize details as needed. For example:
        StringBuilder sb = new StringBuilder();
        sb.append("UserInfo{");
        sb.append("game_count=").append(this.getGame_count()).append(", ");
        sb.append("players=").append(this.getPlayers()).append(", ");
        sb.append("ownedGames=").append(this.getOwenedAndPlayedGames());
        sb.append("}");
        return sb.toString();
    }
}
