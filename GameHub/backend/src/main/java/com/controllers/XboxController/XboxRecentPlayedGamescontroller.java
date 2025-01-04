package com.controllers.XboxController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.XboxModel.RecentGamesXbox;
import com.services.TokenService;

@RestController
@RequestMapping("/api/xbox/recent-games")
public class XboxRecentPlayedGamescontroller {
    private static final String RECENT_GAMES_URL = "https://titlehub.xboxlive.com/users/xuid({xuid})/titles/titlehistory/decoration/detail?maxItems=5";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    /*
     * requires headers: 
     * Authorization: XBL3.0 x={userhash};{token}
     * x-xbl-contract-version: 2
     * Accept-Language: en-US
     * User XUID
     */
    @GetMapping
    public ResponseEntity<?> getRecentGames(){
        try{
            String authHeader = tokenService.getXboxAuthorizationHeader();
            String xuid = tokenService.getXuid();
            if(xuid == null){
                System.out.println("XUID not found");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("XUID not found");
            }
            System.out.println("RECENT GAMES: " + xuid);
            // send the request 
            // call api and only get 10 (first)friends 
            String url = RECENT_GAMES_URL.replace("{xuid}", xuid);
            System.out.println("Request URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set("x-xbl-contract-version", "2"); 
            headers.set("Accept-Language", "en-US"); // Add Accept-Language header
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            // parse json response -
            ObjectMapper mapper = new ObjectMapper();
            RecentGamesXbox recentGames = mapper.readValue(response.getBody(), RecentGamesXbox.class);
            // return response = 
            return ResponseEntity.ok(recentGames);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch recent games");
        }  
    }
}
