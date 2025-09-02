package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.Steam.SteamUserProfile.UserInfo;
import com.models.Steam.SteamUserProfile.UserOwnedAndPlayedGames;

@Service
public class SteamUserService {
    @Autowired
    private RestTemplate restTemplate;

    public SteamUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    private final String STEAM_API_KEY = "79292D739A20F25E05BC6F85697FCB2C"; 

    public UserInfo getUserInfo(String steamID) {
        UserInfo userInfo = new UserInfo();
        List<SteamProfile> players = new ArrayList<>();
        try {
            String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" 
                         + STEAM_API_KEY + "&steamids=" + steamID;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("response") && response.get("response").has("players")) {
                JsonNode playersNode = response.get("response").get("players");
                if (playersNode.isArray() && playersNode.size() > 0) {
                    JsonNode firstPlayer = playersNode.get(0);
                    SteamProfile player = new SteamProfile();
                    player.setSteamid(getTextValue(firstPlayer, "steamid", ""));
                    player.setCommunityvisibilitystate(getIntValue(firstPlayer, "communityvisibilitystate", 0));
                    player.setProfilestate(getIntValue(firstPlayer, "profilestate", 0));
                    player.setPersonaname(getTextValue(firstPlayer, "personaname", ""));
                    player.setProfileurl(getTextValue(firstPlayer, "profileurl", ""));
                    player.setAvatar(getTextValue(firstPlayer, "avatar", ""));
                    player.setAvatarmedium(getTextValue(firstPlayer, "avatarmedium", ""));
                    player.setAvatarfull(getTextValue(firstPlayer, "avatarfull", ""));
                    player.setAvatarhash(getTextValue(firstPlayer, "avatarhash", ""));
                    player.setLastlogoff(getLongValue(firstPlayer, "lastlogoff", 0L));
                    player.setPersonastate(getIntValue(firstPlayer, "personastate", 0));
                    player.setRealname(getTextValue(firstPlayer, "realname", ""));
                    player.setPrimaryclanid(getTextValue(firstPlayer, "primaryclanid", ""));
                    player.setTimecreated(getLongValue(firstPlayer, "timecreated", 0L));
                    player.setPersonastateflags(getIntValue(firstPlayer, "personastateflags", 0));
                    players.add(player);
                    userInfo.setPlayers(players);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    // get user steam played/owned games
    public UserInfo getUserOnwedAndPlayedGames(String steamId) {
    UserInfo userInfo = new UserInfo();
    List<UserOwnedAndPlayedGames> ownedAndPlayedGames = new ArrayList<>();
    try {
        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" 
                     + STEAM_API_KEY + "&steamid=" + steamId + "&include_appinfo=1&include_played_free_games=1";
        if (steamId == null) {
            return null;
        }
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        if (response != null && response.has("response")) {
            JsonNode responseNode = response.get("response");
            if (responseNode.has("game_count")) {
                userInfo.setGame_count(getIntValue(responseNode, "game_count", 0));
            }
            if (responseNode.has("games")) {
                JsonNode gamesArray = responseNode.get("games");
                if (gamesArray.isArray()) {
                    int loopLimit = Math.min(gamesArray.size(), 5); // only process up to 5 games
                    for (int i = 0; i < loopLimit; i++) {
                        JsonNode gameNode = gamesArray.get(i);
                        UserOwnedAndPlayedGames game = new UserOwnedAndPlayedGames();
                        game.setAppid(getIntValue(gameNode, "appid", 0));
                        game.setName(getTextValue(gameNode, "name", ""));
                        game.setPlaytime_forever(getIntValue(gameNode, "playtime_forever", 0));
                        game.setImg_icon_url(getTextValue(gameNode, "img_icon_url", ""));
                        
                        JsonNode communityNode = gameNode.get("has_community_visible_stats");
                        game.setHas_community_visible_stats(communityNode != null && !communityNode.isNull() ? communityNode.asBoolean() : false);
                        
                        game.setPlaytime_windows_forever(getIntValue(gameNode, "playtime_windows_forever", 0));
                        game.setPlaytime_mac_forever(getIntValue(gameNode, "playtime_mac_forever", 0));
                        game.setPlaytime_linux_forever(getIntValue(gameNode, "playtime_linux_forever", 0));
                        game.setPlaytime_deck_forever(getIntValue(gameNode, "playtime_deck_forever", 0));
                        game.setRtime_last_played(getIntValue(gameNode, "rtime_last_played", 0));
                        game.setPlaytime_disconnected(getIntValue(gameNode, "playtime_disconnected", 0));
                        
                        ownedAndPlayedGames.add(game);
                    }
                    userInfo.setOwenedAndPlayedGames(ownedAndPlayedGames);
                }
            }
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return userInfo;
    }

    // get recent played games - 
    public UserInfo getRecentPlayedGames(String steamId){
        UserInfo userInfo = new UserInfo();
        List<UserOwnedAndPlayedGames> recentPlayedGames = new ArrayList<>();
        try{
            /*
             * url - 
             * https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/
             * ?key=79292D739A20F25E05BC6F85697FCB2C&steamid=76561199052752509
             */
            String url = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=" 
                         + STEAM_API_KEY + "&steamid=" + steamId;
            if(steamId == null){
                return null;
            }
            // if the result is less than 5.. append the rest of the games from 
            // getUserOnwedAndPlayedGames
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if(response != null && response.has("response")){
                JsonNode responseNode = response.get("response");
                if(responseNode.has("games")){
                    JsonNode gamesArray = responseNode.get("games");
                    if(gamesArray.isArray()){
                        int loopLimit = Math.min(gamesArray.size(), 5); // only process up to 5 games
                        for(int i = 0; i < loopLimit; i++){
                            JsonNode gameNode = gamesArray.get(i);
                            UserOwnedAndPlayedGames game = new UserOwnedAndPlayedGames();
                            game.setAppid(getIntValue(gameNode, "appid", 0));
                            game.setName(getTextValue(gameNode, "name", ""));
                            game.setPlaytime_2weeks(getIntValue(gameNode, "playtime_2weeks", 0));
                            game.setPlaytime_forever(getIntValue(gameNode, "playtime_forever", 0));
                            game.setImg_icon_url(getTextValue(gameNode, "img_icon_url", ""));
                            recentPlayedGames.add(game);
                        }
                    }
                }
                // if we get less than 5 games, get the rest from getUserOnwedAndPlayedGames
                if(recentPlayedGames.size() < 5){
                    UserInfo userOwnedGames = getUserOnwedAndPlayedGames(steamId);
                    if(userOwnedGames != null && userOwnedGames.getOwenedAndPlayedGames() != null){
                        List<UserOwnedAndPlayedGames> ownedGames = userOwnedGames.getOwenedAndPlayedGames();
                        for (UserOwnedAndPlayedGames game : ownedGames) {
                            // Check if the game is already in recentPlayedGames by comparing appid.
                            boolean alreadyExists = recentPlayedGames.stream()
                            .anyMatch(g -> g.getAppid() == game.getAppid());
                            if(!alreadyExists){
                                recentPlayedGames.add(game);
                                if(recentPlayedGames.size() >= 5){
                                    break; // Stop if we have 5 games
                                }
                            }
                        }
                    }
                }
                userInfo.setOwenedAndPlayedGames(recentPlayedGames);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }
    // Helper methods to safely extract values from JsonNode
    private String getTextValue(JsonNode node, String field, String defaultValue) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asText() : defaultValue;
    }

    private int getIntValue(JsonNode node, String field, int defaultValue) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asInt() : defaultValue;
    }

    private long getLongValue(JsonNode node, String field, long defaultValue) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asLong() : defaultValue;
    }
}
