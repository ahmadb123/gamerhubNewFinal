package com.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class XboxAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String XBL_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate";
    private static final String XSTS_AUTH_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";

    public String getXstsTokenFromMsToken(String msToken) {
        String xblToken = getXblToken(msToken);
        return getXstsToken(xblToken);
    }

    private String getXblToken(String msToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        Map<String, Object> body = Map.of(
                "RelyingParty", "http://auth.xboxlive.com",
                "TokenType", "JWT",
                "Properties", Map.of(
                        "AuthMethod", "RPS",
                        "SiteName", "user.auth.xboxlive.com",
                        "RpsTicket", "d=" + msToken
                )
        );
    
        ResponseEntity<Map> response = restTemplate.exchange(XBL_AUTH_URL, HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);
    
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to retrieve XBL token: Status - " + response.getStatusCode());
            System.err.println("Response Body: " + response.getBody());
            throw new RuntimeException("Failed to retrieve XBL token");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to retrieve XBL token: Status - " + response.getStatusCode());
            System.err.println("Response Body: " + response.getBody()); // Log full response
            throw new RuntimeException("Failed to retrieve XBL token");
        }
        
    
        Map<String, Object> responseBody = response.getBody();
        System.out.println("XBL Response: " + responseBody);
        if (responseBody == null || !responseBody.containsKey("Token")) {
            throw new RuntimeException("XBL token not found in response: " + responseBody);
        }
    
        return (String) responseBody.get("Token");
    }
    
    private String getXstsToken(String xblToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "RelyingParty", "http://xboxlive.com",
                "TokenType", "JWT",
                "Properties", Map.of(
                        "SandboxId", "RETAIL",
                        "UserTokens", new String[]{xblToken}
                )
        );

        ResponseEntity<Map> response = restTemplate.exchange(XSTS_AUTH_URL, HttpMethod.POST,
                new HttpEntity<>(body, headers), Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to retrieve XSTS token");
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("Token")) {
            throw new RuntimeException("XSTS token not found in response");
        }

        return (String) responseBody.get("Token");
    }
}
