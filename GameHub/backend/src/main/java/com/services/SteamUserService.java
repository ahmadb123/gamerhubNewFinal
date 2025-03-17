package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.Steam.SteamUserProfile.Players;
import com.models.Steam.SteamUserProfile.UserInfo;

@Service
public class SteamUserService {
    private final String STEAM_API_KEY = "79292D739A20F25E05BC6F85697FCB2C"; 

    public UserInfo getUserInfo(String steamID){
        UserInfo userInfo = new UserInfo();
        List<Players> players = new ArrayList<>();
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
                Players player = new Players();
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
}
