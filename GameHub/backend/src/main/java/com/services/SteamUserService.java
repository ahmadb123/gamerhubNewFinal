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

    public UserInfo getUserInfo(String steamID){
        UserInfo userInfo = new UserInfo();
        List<SteamProfile> players = new ArrayList<>();
        try{
            /*
             * Params for the Steam API
             * 1. steamid
             * 2. API key
             */

            String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" 
                         + STEAM_API_KEY + "&steamids=" + steamID;
            // call the api - 
            RestTemplate restTemplate = new RestTemplate();
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            // get the players from the response
            if((response != null && response.has("response")) && response.get("response").has("players")){
                JsonNode playerNode = response.get("response").get("players");
                SteamProfile player = new SteamProfile();
                player.setSteamid(playerNode.get(0).get("steamid").asText());
                player.setCommunityvisibilitystate(playerNode.get(0).get("communityvisibilitystate").asInt());
                player.setProfilestate(playerNode.get(0).get("profilestate").asInt());
                player.setPersonaname(playerNode.get(0).get("personaname").asText());
                player.setProfileurl(playerNode.get(0).get("profileurl").asText());
                player.setAvatar(playerNode.get(0).get("avatar").asText());
                player.setAvatarmedium(playerNode.get(0).get("avatarmedium").asText());
                player.setAvatarfull(playerNode.get(0).get("avatarfull").asText());
                player.setAvatarhash(playerNode.get(0).get("avatarhash").asText());
                player.setLastlogoff(playerNode.get(0).get("lastlogoff").asLong());
                player.setPersonastate(playerNode.get(0).get("personastate").asInt());
                player.setRealname(playerNode.get(0).get("realname").asText());
                player.setPrimaryclanid(playerNode.get(0).get("primaryclanid").asText());
                player.setTimecreated(playerNode.get(0).get("timecreated").asLong());
                player.setPersonastateflags(playerNode.get(0).get("personastateflags").asInt());
                players.add(player);
                userInfo.setPlayers(players);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    // get user steam played/owned games - 
    public UserInfo getUserOnwedAndPlayedGames(String steamId){
        UserInfo userInfo = new UserInfo();
        List<UserOwnedAndPlayedGames> ownedAndPlayedGames = new ArrayList<>();
        try{
            /*
             * Params for the Steam API
             * steamid
             * API key
             * include_appinfo
             * include_played_free_games
             */
            String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" 
                         + STEAM_API_KEY + "&steamid=" + steamId + "&include_appinfo=1&include_played_free_games=1";
            if(steamId == null){
                return null;
            }
            // call the api -
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            // get games and game count - 
            if(response != null && response.has("response") && response.get("response").has("game_count")){
                userInfo.setGame_count(response.get("response").get("game_count").asInt());
            }
            if(response != null && response.has("response") && response.get("response").has("games")){
                for(int i = 0; i < response.get("response").get("games").size(); i++){
                    UserOwnedAndPlayedGames game = new UserOwnedAndPlayedGames();
                    game.setAppid(response.get("response").get("games").get(i).get("appid").asInt());
                    game.setName(response.get("response").get("games").get(i).get("name").asText());
                    game.setPlaytime_forever(response.get("response").get("games").get(i).get("playtime_forever").asInt());
                    game.setImg_icon_url(response.get("response").get("games").get(i).get("img_icon_url").asText());
                    {
                        // Retrieve the current game node
                        JsonNode gameNode = response.get("response").get("games").get(i);
                        JsonNode hasCommunity = gameNode.get("has_community_visible_stats");
                        game.setHas_community_visible_stats(hasCommunity != null && !hasCommunity.isNull() ? hasCommunity.asBoolean() : false);
                    }
                    game.setPlaytime_windows_forever(response.get("response").get("games").get(i).get("playtime_windows_forever").asInt());
                    game.setPlaytime_mac_forever(response.get("response").get("games").get(i).get("playtime_mac_forever").asInt());
                    game.setPlaytime_linux_forever(response.get("response").get("games").get(i).get("playtime_linux_forever").asInt());
                    game.setPlaytime_deck_forever(response.get("response").get("games").get(i).get("playtime_deck_forever").asInt());
                    game.setRtime_last_played(response.get("response").get("games").get(i).get("rtime_last_played").asInt());
                    game.setPlaytime_disconnected(response.get("response").get("games").get(i).get("playtime_disconnected").asInt());
                    ownedAndPlayedGames.add(game);
                }
                userInfo.setOwenedAndPlayedGames(ownedAndPlayedGames);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }
}
