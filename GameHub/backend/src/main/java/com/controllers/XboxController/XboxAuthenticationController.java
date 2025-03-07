package com.controllers.XboxController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.XboxModel.CallBackResponse;
import com.models.XboxModel.LoginResponse;
import com.models.XboxModel.MobileLoginResponse;
import com.services.OAuth2Service;
import com.services.TokenService;
import com.utility.PKCEUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class XboxAuthenticationController {

    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private TokenService tokenService;
    

    // Redirect to Xbox Authentication URL (web)
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpSession session) throws Exception {
        // Generate code verifier and challenge
        String codeVerifier = PKCEUtil.generateCodeVerifier();
        String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);

        // Store the code verifier in the session
        session.setAttribute("code_verifier", codeVerifier);

        // Generate the authorization URL
        String url = oAuth2Service.generateAuthUrl(codeChallenge);

        LoginResponse response = new LoginResponse(url);
        return ResponseEntity.ok(response);
    }

    // Handle the callback from the authentication process (web)
    @GetMapping("/callback")
    public ResponseEntity<CallBackResponse> handleCallback(
        @RequestParam(value = "code", required = false) String code,
        HttpSession session) {

        CallBackResponse callBackResponse = new CallBackResponse();

        if (code == null) {
            callBackResponse.setError("Authorization code is missing.");
            return ResponseEntity.badRequest().body(callBackResponse);
        }

        try {
            String codeVerifier = (String) session.getAttribute("code_verifier");
            if (codeVerifier == null) {
                throw new RuntimeException("Code verifier not found in session.");
            }

            ResponseEntity<String> exchangeCode = oAuth2Service.exchangeCodeForToken(code, codeVerifier);
            if (!exchangeCode.getStatusCode().is2xxSuccessful()) {
                callBackResponse.setError("Failed to exchange authorization code.");
                return ResponseEntity.badRequest().body(callBackResponse);
            }

            String accessToken = oAuth2Service.parseAccessToken(exchangeCode.getBody());
            tokenService.setAccessToken(accessToken);

            tokenService.exchangeAccessTokenForXboxTokens(accessToken);
            String uhs = tokenService.getUhs();
            String xstsToken = tokenService.getXstsToken();

            callBackResponse.setSuccess("true");
            callBackResponse.setUhs(uhs);
            callBackResponse.setXSTS_token(xstsToken);

            return ResponseEntity.ok(callBackResponse);
        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.setError("An error occurred during token exchange.");
            return ResponseEntity.badRequest().body(callBackResponse);
        }
    }
    // Mobile flow ------------------------------------------------------------
    @GetMapping("/mobile-login")
    public ResponseEntity<MobileLoginResponse> mobileLogin() throws Exception {
        String codeVerifier = PKCEUtil.generateCodeVerifier();
        String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);
        
        String url = oAuth2Service.generateAuthUrlForMobiles(codeChallenge);
        return ResponseEntity.ok(new MobileLoginResponse(url, codeVerifier));
    }

    @PostMapping("/mobile-callback")
    public ResponseEntity<CallBackResponse> handleMobileCallback(
        @RequestParam("code") String code,
        @RequestParam("code_verifier") String codeVerifier) {
        
        CallBackResponse response = new CallBackResponse();
        
        try {
            // 1. Exchange code for Microsoft token
            ResponseEntity<String> tokenResponse = oAuth2Service.exchangeCodeForTokenMobile(
                code,
                codeVerifier,
                "my-new-project://auth" // Must match exactly
            );
            
            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                response.setError("Token exchange failed: " + tokenResponse.getBody());
                return ResponseEntity.badRequest().body(response);
            }

            // 2. Parse Microsoft access token
            String accessToken = oAuth2Service.parseAccessToken(tokenResponse.getBody());
            
            // 3. Exchange for Xbox tokens
            tokenService.exchangeAccessTokenForXboxTokens(accessToken);
            
            // 4. Get final tokens
            response.setSuccess("true");
            response.setUhs(tokenService.getUhs());
            response.setXSTS_token(tokenService.getXstsToken());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setError("Authentication flow failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}