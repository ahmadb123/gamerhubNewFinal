package com.controllers.SteamController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.Steam.SteamUserProfile.UserInfo;
import com.services.SteamUserService;

@RestController
@RequestMapping("/api/steam/userinfo")
public class SteamUserInfoController {
    @Autowired
    private SteamUserService steamUserInfoService;

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestParam String steamId) {
        try {
            UserInfo userInfo = steamUserInfoService.getUserInfo(steamId);
            if(userInfo == null) {
                return ResponseEntity.status(404).body("Steam profile not found");
            }
            return ResponseEntity.ok(userInfo);
        } catch(Exception e){
            return ResponseEntity.status(500).body("Error retrieving user info: " + e.getMessage());
        }
    }
}
