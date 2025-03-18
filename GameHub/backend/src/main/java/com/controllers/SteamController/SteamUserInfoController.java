package com.controllers.SteamController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Repository.SteamProfileRepository;
import com.Repository.UserRepository;
import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.Steam.SteamUserProfile.UserInfo;
import com.models.Steam.SteamUserProfile.SteamUserFriends.FriendsList;
import com.models.UserModel.User;
import com.services.SteamProfileService;
import com.services.SteamUserFriendsListService;
import com.services.SteamUserService;
import com.services.UserLinkedProfilesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/steam/userinfo")
public class SteamUserInfoController {
    @Autowired
    private SteamUserService steamUserInfoService;
    @Autowired
    private SteamUserFriendsListService getFriends;
    @Autowired
    private SteamProfileService steamProfileService;
    @Autowired
    private JWT jwt;
    @Autowired
    private SteamProfileRepository steamProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLinkedProfilesService userLinkedProfilesService;

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestParam String steamId, @RequestHeader("Authorization") String authHeader) {
        try {
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            // check if token is valid
            if(token == null || token.isEmpty()){
                return ResponseEntity.status(401).body("Invalid token");
            }
            String username = jwt.extractUsername(token);
            Long userId = jwt.extractUserId(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserInfo userInfo = steamUserInfoService.getUserInfo(steamId);
            if(userInfo == null) {
                return ResponseEntity.status(404).body("Steam profile not found");
            }

            SteamProfile steamProfile = userInfo.getPlayers().get(0);
            // save to db - 
            steamProfileService.saveProfile(steamProfile, user);
            // also save to the linked list profiles for each user-
            if(userId != null){
                User foundUserId = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                userLinkedProfilesService.addSteamProfileToLinkedProfiles(foundUserId);
            }
            return ResponseEntity.ok(userInfo);
        } catch(Exception e){
            return ResponseEntity.status(500).body("Error retrieving user info: " + e.getMessage());
        }
    }
    // get user played and owned games
    @GetMapping("/getUserGameInfo")
    public ResponseEntity<?> getPlayedAndUsedGames(@RequestHeader("Authorization") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            // get logged in user id - 
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token); // get user id from token
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<SteamProfile> profile = steamProfileRepository.findSteamUserByUserId(user.getId());
            if(!profile.isPresent()){
                return ResponseEntity.status(404).body("Steam profile not found");
            }
            // get steam id -
            String steamId = profile.get().getSteamid();
            // call the api from service - 
            UserInfo gamesDetails = steamUserInfoService.getUserOnwedAndPlayedGames(steamId);
            // return response in json - 
            return ResponseEntity.ok(gamesDetails);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error retrieving user info: " + e.getMessage());
        }
    }

    @GetMapping("/recent-played-games")
    public ResponseEntity<?> getRecentPlayedGames(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token); // get user id from token
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<SteamProfile> profile = steamProfileRepository.findSteamUserByUserId(user.getId());
            if (!profile.isPresent()) {
                return ResponseEntity.status(404).body("Steam profile not found");
            }
            String steamId = profile.get().getSteamid();
            UserInfo recentPlayedGames = steamUserInfoService.getRecentPlayedGames(steamId);
            return ResponseEntity.ok(recentPlayedGames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving recent played games: " + e.getMessage());
        }
    }

    @GetMapping("/get-friends")
    public ResponseEntity<?> getFriendsList(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token); // get user id from token
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<SteamProfile> profile = steamProfileRepository.findSteamUserByUserId(user.getId());
            if (!profile.isPresent()) {
                return ResponseEntity.status(404).body("Steam profile not found");
            }
            String steamId = profile.get().getSteamid();
            FriendsList friendsList = getFriends.getFriendsList(steamId);
            return ResponseEntity.ok(friendsList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving friends list: " + e.getMessage());
        }
    }
}
