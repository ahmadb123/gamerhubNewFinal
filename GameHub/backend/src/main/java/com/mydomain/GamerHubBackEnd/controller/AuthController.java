package com.mydomain.GamerHubBackEnd.controller;

import com.mydomain.GamerHubBackEnd.models.xboxLoginRequest;
import com.mydomain.GamerHubBackEnd.models.PSNLoginRequest;
import com.mydomain.GamerHubBackEnd.models.SteamLoginRequest;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_API = "https://28282.playfabapi.com";
    private static final String SECRET_KEY = "JY9ZPRZAYYH98ZB6CG8Z1GCZNKGHP4DXW4ERAAXOARXXNIAS5S";

    @PostMapping("/xbox")
    public ResponseEntity<?> loginWithXbox(@RequestBody xboxLoginRequest request) {
        return sendToPlayFab("/Server/LoginWithXbox", request);
    }

    @PostMapping("/psn")
    public ResponseEntity<?> loginWithPSN(@RequestBody PSNLoginRequest request) {
        return sendToPlayFab("/Server/LoginWithPSN", request);
    }

    @PostMapping("/steam")
    public ResponseEntity<?> loginWithSteam(@RequestBody SteamLoginRequest request) {
        return sendToPlayFab("/Server/LoginWithSteamId", request);
    }

    private ResponseEntity<?> sendToPlayFab(String endpoint, Object requestBody) {
        String url = BASE_API + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-SecretKey", SECRET_KEY);

        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
