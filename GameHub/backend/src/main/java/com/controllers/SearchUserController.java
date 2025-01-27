package com.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.XboxProfileDTO;
import com.dto.XboxRecentGameDTO;
import com.models.DataModelAccountLinks.XboxRecentGame;
import com.services.XboxProfileService;
import com.services.XboxRecentGamesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/search")

public class SearchUserController {
    @Autowired
    private JWT jwt;
    @Autowired
    private XboxProfileService xboxProfileService; 
    @Autowired
    private XboxRecentGamesService xboxRecentGamesService;
    @GetMapping
    public ResponseEntity<?> searchUser(@RequestParam String username, @RequestHeader("Authorization") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            String loggedInUser = jwt.extractUsername(token);
            if(loggedInUser == null){
                System.out.println("Invalid token or username not found");
                return ResponseEntity.status(401).body("Invalid token or username not found");
            }
            if(username == null){
                System.out.println("Username not found");
                return ResponseEntity.status(404).body("User not found");
            }
            XboxProfileDTO dto = xboxProfileService.getSearchedProfileData(username);
            List<XboxRecentGameDTO> recentPlayedGames = xboxRecentGamesService.getRecentGamesByUsername(username);

            // debugging- 
            System.out.println("User " + dto.getGamertag());
            System.out.println("Recent Played Games: " + recentPlayedGames);
            // mapping - 
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("profile", dto);
            responseData.put("recentPlayedGames", recentPlayedGames);
           // 4) Return that map as JSON
            return ResponseEntity.ok(responseData); 
        }catch(Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}


