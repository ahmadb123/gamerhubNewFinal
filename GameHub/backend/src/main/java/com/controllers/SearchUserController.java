package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.XboxProfileDTO;

import com.services.XboxProfileService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/search")

public class SearchUserController {
    @Autowired
    private JWT jwt;
    @Autowired
    private XboxProfileService xboxProfileService; 

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
            // return user xbox profile - 
            return ResponseEntity.ok(dto);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}


