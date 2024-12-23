// /*
//  * This page mainly codes the oauth2 for xbox users-
//  * the authentication returns a token tha
//  */
// package com.controllers;

// import com.services.OAuth2Service;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.services.TokenService;
// import com.utility.PKCEUtil;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.servlet.view.RedirectView;
// import jakarta.servlet.http.HttpSession;

// @RestController
// @RequestMapping("/api/auth")
// public class XboxAuthController {
//     @Autowired
//     private TokenService tokenService;
//     @GetMapping("/login")
//     public RedirectView login(HttpSession session) throws Exception {
//         String codeVerifier = PKCEUtil.generateCodeVerifier();
//         String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);

//         // Store the code verifier in the session
//         session.setAttribute("code_verifier", codeVerifier);

//         String url = "https://login.live.com/oauth20_authorize.srf" +
//             "?client_id=" + CLIENT_ID +
//             "&response_type=code" +
//             "&redirect_uri=" + REDIRECT_URI +
//             "&scope=XboxLive.signin XboxLive.offline_access" + 
//             "&code_challenge=" + codeChallenge +
//             "&code_challenge_method=S256";
//         return new RedirectView(url);
//     }

//     @GetMapping("/callback")
//     public ResponseEntity<Map<String, String>> handleCallback(
//         @RequestParam(value = "code", required = false) String code,
//         HttpSession session) {
//         Map<String, String> response = new HashMap<>();
//         if (code == null) {
//             response.put("error", "missing_code");
//             return ResponseEntity.badRequest().body(response);
//         }
    
//         try {
//             String codeVerifier = (String) session.getAttribute("code_verifier");
//             if (codeVerifier == null) {
//                 throw new RuntimeException("Code verifier not found in session.");
//             }
    
//             // Step 1: Exchange code for Microsoft access token
//             ResponseEntity<String> tokenResponse = exchangeCodeForToken(code, codeVerifier);
//             String accessToken = parseAccessToken(tokenResponse.getBody());
//             tokenService.setAccessToken(accessToken);
//             System.out.println("Microsoft Access Token: " + accessToken);
    
//             // Step 2: Exchange for Xbox tokens
//             tokenService.exchangeAccessTokenForXboxTokens(accessToken);
//             System.out.println("XBL and XSTS tokens exchanged successfully");
    
//             // Step 3: Add uhs and XSTS_token to the response
//             String uhs = tokenService.getUhs();
//             String xstsToken = tokenService.getXstsToken();
//             response.put("success", "true");
//             response.put("uhs", uhs);
//             response.put("XSTS_token", xstsToken);
    
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.put("error", "token_exchange_failed");
//             return ResponseEntity.badRequest().body(response);
//         }
//     }
    
    

//     private ResponseEntity<String> exchangeCodeForToken(String code, String codeVerifier) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//         headers.add("Origin", "http://localhost:3000"); // Add Origin header

//         String body = "client_id=" + CLIENT_ID +
//                       "&redirect_uri=" + REDIRECT_URI +
//                       "&code=" + code +
//                       "&code_verifier=" + codeVerifier +
//                       "&grant_type=authorization_code";

//         HttpEntity<String> request = new HttpEntity<>(body, headers);

//         try {
//             System.out.println("Request Body: " + body);
//             ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);
//             System.out.println("Token Response: " + response.getBody());
//             return response;
//         } catch (Exception e) {
//             System.err.println("Error during token exchange: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Token exchange failed", e);
//         }
//     }

//     private String parseAccessToken(String responseBody) throws Exception {
//         ObjectMapper objectMapper = new ObjectMapper();
//         JsonNode jsonNode = objectMapper.readTree(responseBody);

//         // Extract and return the access token
//         if (jsonNode.has("access_token")) {
//             return jsonNode.get("access_token").asText();
//         } else {
//             throw new RuntimeException("Access token not found in the response");
//         }
//     }
// }
