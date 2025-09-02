package com.services;
/*
 * This class is used to get friends list from Steam API
 * Endpoint - https://api.steampowered.com/ISteamUser/GetFriendList/v0001/
*/

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.Steam.SteamUserProfile.SteamUserFriends.Friends;
import com.models.Steam.SteamUserProfile.SteamUserFriends.FriendsList;
@Service
public class SteamUserFriendsListService {
    /*
     * Key paramaeters
     * key - API key
     * steamid - Steam ID
     * &relationship=friend
     */

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SteamUserService userService;

    public SteamUserFriendsListService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private final String STEAM_API_KEY = "79292D739A20F25E05BC6F85697FCB2C";

    public FriendsList getFriendsList(String steamID){
        FriendsList friendsList = new FriendsList();
        List<Friends> friends = new ArrayList<>();
        try{
           String url = "https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + STEAM_API_KEY + "&steamid=" + steamID + "&relationship=friend";
           JsonNode response = restTemplate.getForObject(url, JsonNode.class);
           if(response != null && response.has("friendslist") && response.get("friendslist").has("friends")){
               JsonNode friendsNode = response.get("friendslist").get("friends");
               if(friendsNode.isArray() && friendsNode.size() > 0){
                   for(JsonNode friendNode : friendsNode){
                       Friends friend = new Friends();
                       friend.setSteamId(friendNode.get("steamid").asText());
                       friend.setRelationship(friendNode.get("relationship").asText());
                       friend.setFriendSince(friendNode.get("friend_since").asLong());
                       friends.add(friend);
                   }
                   friendsList.setFriends(friends);
               }
           }
           // get the rest of the data-> 
           for(int i = 0; i < friends.size(); i++){
                String friendSteamId = friends.get(i).getSteamId();
                // get the user info for each friend
                userService.getUserInfo(friendSteamId);
                // store // these fields dont belong from the steam api-> 
                // private String friendName;
                // private String friendAvatar;
                // private String friendProfileUrl;
                friends.get(i).setFriendName(userService.getUserInfo(friendSteamId).getPlayers().get(0).getPersonaname());
                friends.get(i).setFriendAvatar(userService.getUserInfo(friendSteamId).getPlayers().get(0).getAvatar());
                friends.get(i).setFriendProfileUrl(userService.getUserInfo(friendSteamId).getPlayers().get(0).getProfileurl());
            }
            // add them to friendslist? 
            friendsList.setFriends(friends);
        }catch(Exception e){
            e.printStackTrace();
        }
        return friendsList;
    }
}
