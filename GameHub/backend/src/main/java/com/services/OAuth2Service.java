/*
 * Microsoft Authentication Library (MSAL) and Microsoft Graph 
 * This class processes the oauth2 microsoft authentication - 
 * using Azure microsoft 
 * Varibales - client id, redirect uri, and token url
 */
package com.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class OAuth2Service {
    private final String CLIENT_ID = "0a23b968-9e79-4cba-b337-862adab7a8e2";
    // private final String REDIRECT_URI = "http://localhost:3000";
    private final String REDIRECT_URI = "my-new-project://auth";
    private final String TOKEN_URL = "https://login.live.com/oauth20_token.srf";

    @Autowired
    private RestTemplate restTemplate;

    //generate authenticationURL
    public String generateAuthUrl(String codeChallenge){
        return "https://login.live.com/oauth20_authorize.srf" +
        "?client_id=" + CLIENT_ID +
        "&response_type=code" +
        "&redirect_uri=" + REDIRECT_URI +
        "&scope=XboxLive.signin XboxLive.offline_access" +
        "&code_challenge=" + codeChallenge +
        "&code_challenge_method=S256";
        
    }

    public String generateAuthUrlForMobiles(String codeChallenge) {
        return "https://login.live.com/oauth20_authorize.srf" +
               "?client_id=" + CLIENT_ID +
               "&response_type=code" +
               "&redirect_uri=" + REDIRECT_URI +
               "&scope=XboxLive.signin XboxLive.offline_access" +
               "&code_challenge=" + codeChallenge +
               "&code_challenge_method=S256";
    }
    

    // exhcnage code for token - 
    public ResponseEntity<String> exchangeCodeForToken(String code, String codeVerifier){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Origin", "http://localhost:3000");
        String body = "client_id=" + CLIENT_ID +
                      "&redirect_uri=" + REDIRECT_URI +
                      "&code=" + code +
                      "&code_verifier=" + codeVerifier +
                      "&grant_type=authorization_code";
        
        HttpEntity<String> request = new HttpEntity<>(body,headers);

        try{
            // for logs - 
            System.out.println("request body:" + body);
            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);
            //logs - 
            System.out.println("Token response" + response.getBody());
            return response;
        }catch(Exception e){
            System.err.println("Error during token exchange: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Token exchange failed", e);
        }
    }

    public ResponseEntity<String> exchangeCodeForTokenMobile(String code, String codeVerifier, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "client_id=" + CLIENT_ID +
                      "&redirect_uri=" + redirectUri +
                      "&code=" + code +
                      "&code_verifier=" + codeVerifier +
                      "&grant_type=authorization_code";
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        try {
            System.out.println("Mobile request body: " + body);
            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);
            System.out.println("Mobile token response: " + response.getBody());
            return response;
        } catch (Exception e) {
            System.err.println("Error during mobile token exchange: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Mobile token exchange failed", e);
        }
    }
    


    // parse token 
    public String parseAccessToken(String responseBody) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if(jsonNode.has("access_token")){
            return jsonNode.get("access_token").asText();
        }else{
            throw new RuntimeException("Access token not found in the response");
        }
    }
}
