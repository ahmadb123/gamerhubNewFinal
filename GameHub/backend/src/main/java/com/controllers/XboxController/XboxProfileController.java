package com.controllers.XboxController;
import com.services.TokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.XboxModel.XboxProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/xbox")
public class XboxProfileController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    private final String PROFILE_API_URL = "https://profile.xboxlive.com/users/me/profile/settings?settings=Gamertag,GameDisplayName,AppDisplayPicRaw,GameDisplayPicRaw,AccountTier,TenureLevel,Gamerscore";

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            // Get Authorization Header
            String authorizationHeader = tokenService.getXboxAuthorizationHeader();
            System.out.println("Authorization Header Used: " + authorizationHeader);

            // Prepare HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("x-xbl-contract-version", "3"); // Required by the Xbox API

            // Send GET Request
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(PROFILE_API_URL, HttpMethod.GET, requestEntity, String.class);

            // Parse Response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode profileUser = rootNode.path("profileUsers").get(0);
            JsonNode settings = profileUser.path("settings");

            // Populate XboxProfile Model
            XboxProfile xboxProfile = new XboxProfile();
            xboxProfile.setId(profileUser.path("id").asText());
            tokenService.setXuid(xboxProfile.getId()); // save the xuid in tokenservice.
            xboxProfile.setGamertag(getSettingValue(settings, "Gamertag"));
            xboxProfile.setGameDisplayName(getSettingValue(settings, "GameDisplayName"));
            xboxProfile.setAppDisplayPicRaw(getSettingValue(settings, "AppDisplayPicRaw"));
            xboxProfile.setGamerscore(Integer.parseInt(getSettingValue(settings, "Gamerscore")));
            xboxProfile.setTenureLevel(Integer.parseInt(getSettingValue(settings, "TenureLevel")));

            return ResponseEntity.ok(xboxProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch profile");
        }
    }

    private String getSettingValue(JsonNode settings, String settingId) {
        for (JsonNode setting : settings) {
            if (setting.path("id").asText().equals(settingId)) {
                return setting.path("value").asText();
            }
        }
        System.out.println("Setting not found: " + settingId);
        return null; // Return null if not found
    }
    
}
