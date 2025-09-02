package com.services.DiscordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DiscordOauth2AuthenticationService {

    @Value("${discord.client.id}")
    private String clientId;

    @Value("${discord.redirect.uri}")
    private String redirectUri;

    @Value("${discord.scope}")
    private String scope;

    @Value("${discord.client.secret}")
    private String clientSecret;  // Needed for token exchange
    // Use the Discord OAuth2 authorization endpoint.
    private final String DISCORD_AUTHORIZATION_ENDPOINT = "https://discord.com/api/oauth2/authorize";

    private final String DISCORD_TOKEN_ENDPOINT = "https://discord.com/api/oauth2/token";

    // RestTemplate to use later for token exchange if needed
    @Autowired
    private RestTemplate restTemplate;

    public DiscordOauth2AuthenticationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
  
    /**
     * Generates the Discord OAuth2 authorization URL.
     * Users are redirected to this URL to begin the authentication process.
     *
     * @return the fully constructed Discord authorization URL.
     */
    public String generateAuthorizationUrl() {
        return DISCORD_AUTHORIZATION_ENDPOINT +
            "?client_id=" + clientId +
            "&redirect_uri=" + encode(redirectUri) +
            "&response_type=code" +
            "&scope=" + encode(scope);
    }

    public String exchangeCodeForToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Origin", "http://localhost:3000");
        String body = "client_id=" + clientId +
                      "&client_secret=" + clientSecret +
                      "&redirect_uri=" + encode(redirectUri) +
                      "&code=" + code +
                      "&grant_type=authorization_code" +
                      "&scope=" + encode(scope);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try{
            // for logs - 
            System.out.println("Request Body: " + body);
            ResponseEntity<?> response = restTemplate.exchange(DISCORD_TOKEN_ENDPOINT, HttpMethod.POST, entity, String.class);
            
            System.out.println("Response: " + response.getBody());
            return response.getBody().toString();
        }catch(Exception e){
            // Handle exceptions
            System.err.println("Error during token exchange: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Simple URL encoder for query parameters.
     */
    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 encoding is not supported", ex);
        }
    }

    // parse access token - 
    public String parseAccessToken(String resBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);
        if(jsonNode.has("access_token")){
            return jsonNode.get("access_token").asText();
        }else{
            throw new RuntimeException("Access token not found in response");
        }
    }
}
