package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.services.UserLinkedProfilesService;
import com.utility.JWT;

/*
 * This class will handle the requests for the user linked profiles
 */

@RestController
@RequestMapping("/api/user/linked-profiles")
public class UserLinkedProfilesController {
    @Autowired
    private JWT jwt;
    @Autowired
    private UserLinkedProfilesService userLinkedProfilesService;
    @GetMapping("/getLinkedProfiles")
    public ResponseEntity<?> getUserLinkedProfiles(@RequestHeader("Authorization") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            // check if token is valid
            if(token == null || token.isEmpty()){
                return ResponseEntity.status(401).body("Invalid token");
            }
            // get the user id from the token
            Long userId = jwt.extractUserId(token);
            // check if user id is valid
            if(userId == null){
                return ResponseEntity.status(401).body("Invalid token");
            }
            // call the service to get the linked profiles
            return ResponseEntity.ok(userLinkedProfilesService.getAllLinkedProfiles(userId));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
