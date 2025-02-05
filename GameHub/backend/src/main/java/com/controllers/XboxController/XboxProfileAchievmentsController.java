package com.controllers.XboxController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.models.XboxProfileAchievements.Achievements;
import com.services.AchievementsService;
import com.services.TokenService;

@RestController
@RequestMapping("/api/xbox/profile/achievements")
public class XboxProfileAchievmentsController {
    private static final String ACHIEVMENTS_URL = "https://achievements.xboxlive.com/users/xuid({xuid})/achievements";

    @Autowired 
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AchievementsService achievementsService;
    @GetMapping
    public ResponseEntity<?> getAchievements(){
        try {
            /*
             * Required headers: 
             * Authorization: Bearer {XBL3.0 userhash and token}
             * X-Xbl-Contract-Version: 2
             */

            // Get Authorization Header
            String authHeader = tokenService.getXboxAuthorizationHeader(); // get uhs and token
            String xuid = tokenService.getXuid(); // get xuid

            if(xuid == null || xuid.isEmpty() || authHeader == null || authHeader.isEmpty()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid xuid");
            }

            String url = ACHIEVMENTS_URL.replace("{xuid}", xuid);
            System.out.println("URL: " + url); // for debugging

            // prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set("X-Xbl-Contract-Version", "2");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            List<Achievements> achievements = achievementsService.getAchievements(xuid, response.getBody());
            return ResponseEntity.ok(achievements);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
