package com.controllers;

import com.models.xboxLoginRequest;
import com.models.PSNLoginRequest;
import com.models.SteamLoginRequest;
import com.services.XboxAuthService;

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

    private final XboxAuthService xboxAuthService;

    public AuthController(XboxAuthService xboxAuthService) {
        this.xboxAuthService = xboxAuthService;
    }

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

    /**
     * New endpoint:
     * Receives msToken from frontend, exchanges it for XSTS token, then logs into PlayFab using that XSTS token.
     */
    @PostMapping("/xboxExchange")
    public ResponseEntity<?> exchangeMsTokenForXboxAndLogin(@RequestBody Map<String, String> body) {
        try {
            String msToken = body.get("msToken");
            if (msToken == null || msToken.isEmpty()) {
                return ResponseEntity.badRequest().body("msToken is required");
            }

            // Get XSTS token from MS token
            String xstsToken = xboxAuthService.getXstsTokenFromMsToken(msToken);

            // Now we have the XSTS token, create a request for PlayFab
            xboxLoginRequest playFabRequest = new xboxLoginRequest();
            playFabRequest.setXboxToken(xstsToken);
            playFabRequest.setCreateAccount(true); // or false, depending on your needs

            // Call PlayFab login with the obtained XSTS token
            return sendToPlayFab("/Server/LoginWithXbox", playFabRequest);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    private ResponseEntity<?> sendToPlayFab(String endpoint, Object requestBody) {
        String url = BASE_API + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-SecretKey", SECRET_KEY);

        HttpEntity<Object> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
