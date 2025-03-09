package com.controllers.XboxController;

import java.util.List;

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
import com.models.GameClipRecordXbox.GameClips;
import com.services.GameClipsServiceXbox;
import com.services.TokenService;

@RestController
@RequestMapping("/api/xbox")
public class XboxGameClipsController {
    private static final String GAME_CLIPS_API ="https://gameclipsmetadata.xboxlive.com/users/xuid({xuid})/clips";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired 
    private GameClipsServiceXbox gameClipsService;
    @GetMapping("/gameclips")
    public ResponseEntity<?> getGameClips(){
        /*
        * requires headers: 
        * Authorization: XBL3.0 x={userhash};{token}
        * x-xbl-contract-version: 2
        * User XUID
        */
        try{
            String authorizationHeader = tokenService.getXboxAuthorizationHeader(); // gets uhs and token
            String xuid = tokenService.getXuid(); // gets user xuid

            if(xuid == null || authorizationHeader == null) {
                System.out.println("XUID or Authorization Header not found");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("XUID or Authorization Header not found");
            }
            String finalUrl = GAME_CLIPS_API.replace("{xuid}", xuid);
            System.out.println("Request URL: " + finalUrl); // for debugging

            // Prepare HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("x-xbl-contract-version", "2"); 

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity, String.class);

            List<GameClips> gameClips = gameClipsService.getGameClips(response.getBody());
            return ResponseEntity.ok(gameClips);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}