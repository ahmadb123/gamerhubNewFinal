package com.controllers.DiscordController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.XboxModel.LoginResponse;
import com.services.DiscordService.DiscordOauth2AuthenticationService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth/discord")
public class DiscordAuthenticationController {
    // This class will handle the authentication process with Discord
    // It will use the Discord API to authenticate users and get their information
    // It will also handle the redirect URI and the authorization code
    // It will use the DiscordOauth2Authentication class to send the authentication request
    // It will use the DiscordRepository class to save the user information in the database
    // It will use the DiscordService class to handle the business logic of the authentication process
    @Autowired 
    private DiscordOauth2AuthenticationService discordOauth2AuthenticationService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpSession session) throws Exception{
        String url = discordOauth2AuthenticationService.generateAuthorizationUrl();
        LoginResponse response = new LoginResponse();
        response.setRedirectUrl(url);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code,
    HttpSession session) {
        // the redirect link will provide code - 
        if(code == null || code.isEmpty()){
            return ResponseEntity.badRequest().body("Missing authorization code");
        }
        // exchange code for token - 
        String tokenResponse = discordOauth2AuthenticationService.exchangeCodeForToken(code);
        if(tokenResponse == null){
            return ResponseEntity.status(500).body("Token exchange failed");
        }
        // get the access token - 
        String accessToken;
        try{
            accessToken = discordOauth2AuthenticationService.parseAccessToken(tokenResponse);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Failed to parse access token");
        }
        return ResponseEntity.status(500).body("Failed to parse access token");
    }
}
