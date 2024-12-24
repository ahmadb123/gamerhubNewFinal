package com.controllers;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.XboxPeopleFriends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.services.TokenService;

@RestController
@RequestMapping("/api/xbox/friends")
public class XboxFriendsController {
    private final String FRIENDS_API_URL = "https://peoplehub.xboxlive.com/users/me/people/social";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenService tokenService; // fetch uhs and token
    @GetMapping("/top-ten")
    public ResponseEntity<?> getTopTenFriends(){
        try{
            // Get Authorization Header
            String authHeader = tokenService.getXboxAuthorizationHeader();
            System.out.println("FRIENDS AUTHORIZATION: " + authHeader);

            // HTTP headers to send with the request - 
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set("x-xbl-contract-version", "3"); 
            headers.set("Accept-Language", "en-US"); // Add Accept-Language header

            // send the request 
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            // call api and only get 10 (first)friends 
            String url = FRIENDS_API_URL;
            System.out.println("Request URL: " + url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            // parse json response - 
            ObjectMapper mapper = new ObjectMapper();
            XboxPeopleFriends friends = mapper.readValue(response.getBody(), XboxPeopleFriends.class);
            // return the response
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch friends");
        }
    }   

    // public ResponseEntity<?> getAllFriends(){

    // }
}
