package com.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class TokenService {
    private String msAccessToken; // Microsoft access token
    private String xblToken; // Xbox Live token
    private String xstsToken; // XSTS token
    private String uhs; // UserHash
    private String xuid; // Xbox User ID

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public String getXuid() {
        return xuid;
    }
    @Autowired
    private RestTemplate restTemplate;

    public void setAccessToken(String token) {
        this.msAccessToken = token;
        System.out.println("Microsoft Access Token set: " + msAccessToken);
    }

    public void exchangeAccessTokenForXboxTokens(String accessToken) {
        try {
            // Exchange Microsoft access token for XBL token
            Map<String, Object> xblRequest = Map.of(
                "RelyingParty", "http://auth.xboxlive.com",
                "TokenType", "JWT",
                "Properties", Map.of(
                    "AuthMethod", "RPS",
                    "SiteName", "user.auth.xboxlive.com",
                    "RpsTicket", "d=" + accessToken
                )
            );

            ResponseEntity<String> xblResponse = restTemplate.postForEntity(
                "https://user.auth.xboxlive.com/user/authenticate", xblRequest, String.class
            );

            JsonNode xblJson = new ObjectMapper().readTree(xblResponse.getBody());
            this.xblToken = xblJson.get("Token").asText();
            this.uhs = xblJson.get("DisplayClaims").get("xui").get(0).get("uhs").asText();
            System.out.println("XBL Token: " + xblToken);
            System.out.println("UHS: " + uhs);

            // Exchange XBL token for XSTS token
            Map<String, Object> xstsRequest = Map.of(
                "RelyingParty", "http://xboxlive.com",
                "TokenType", "JWT",
                "Properties", Map.of(
                    "SandboxId", "RETAIL",
                    "UserTokens", List.of(xblToken)
                )
            );

            ResponseEntity<String> xstsResponse = restTemplate.postForEntity(
                "https://xsts.auth.xboxlive.com/xsts/authorize", xstsRequest, String.class
            );

            JsonNode xstsJson = new ObjectMapper().readTree(xstsResponse.getBody());
            this.xstsToken = xstsJson.get("Token").asText();
            System.out.println("XSTS Token: " + xstsToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to exchange Xbox tokens: " + e.getMessage());
        }
    }

    public String getXboxAuthorizationHeader() {
        System.out.println("Authorization Header being returned: XBL3.0 x=" + uhs + ";" + xstsToken);
        return "XBL3.0 x=" + uhs + ";" + xstsToken;
    }

    // Getters for private fields
    public String getMsAccessToken() {
        return msAccessToken;
    }

    public String getXblToken() {
        return xblToken;
    }

    public String getXstsToken() {
        return xstsToken;
    }

    public String getUhs() {
        return uhs;
    }
}
