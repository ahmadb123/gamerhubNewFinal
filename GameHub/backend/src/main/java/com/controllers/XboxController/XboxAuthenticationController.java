/*
 * This page takes care of Oauth2 authentication
 * redirecting the page to xbox live authentication
 * returns xbox token which then parsed into an access token and then exchanged with an xbox login token
 * the xbox login token requires two codes - 
 * 1. uhs code, and the token code.
 */
package com.controllers.XboxController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.XboxModel.CallBackResponse;
import com.models.XboxModel.LoginResponse;
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

    // Redirect to Xbox Authentication URL
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

    // Handle the callback from the authentication process
    @GetMapping("/callback")
    public ResponseEntity<CallBackResponse> handleCallback(
        @RequestParam(value = "code", required = false) String code,
        HttpSession session) {

        // Initialize the response object
        CallBackResponse callBackResponse = new CallBackResponse();

        // Validate if the code is present
        if (code == null) {
            callBackResponse.setError("Authorization code is missing.");
            return ResponseEntity.badRequest().body(callBackResponse);
        }

        try {
            // Retrieve the code verifier from the session
            String codeVerifier = (String) session.getAttribute("code_verifier");
            if (codeVerifier == null) {
                throw new RuntimeException("Code verifier not found in session.");
            }

            // Exchange the authorization code for an access token
            ResponseEntity<String> exchangeCode = oAuth2Service.exchangeCodeForToken(code, codeVerifier);
            if (!exchangeCode.getStatusCode().is2xxSuccessful()) {
                callBackResponse.setError("Failed to exchange authorization code.");
                return ResponseEntity.badRequest().body(callBackResponse);
            }

            // Parse the access token
            String accessToken = oAuth2Service.parseAccessToken(exchangeCode.getBody());
            tokenService.setAccessToken(accessToken);

            tokenService.exchangeAccessTokenForXboxTokens(accessToken);
            // Exchange for Xbox tokens
            String uhs = tokenService.getUhs();
            String xstsToken = tokenService.getXstsToken();

            // Populate the success response
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
}
