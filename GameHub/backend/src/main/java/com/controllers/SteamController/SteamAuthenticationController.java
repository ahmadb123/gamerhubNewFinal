package com.controllers.SteamController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Steam.SteamAuthResponse;
import com.models.Steam.SteamLoginResponse;
import com.services.SteamOpenIDService;

@RestController
@RequestMapping("steam")
public class SteamAuthenticationController {

    @Autowired
    private SteamOpenIDService steamOpenIDService;

    // Inject the frontend return URL from properties
    @Value("${steam.openid.returnUrl}")
    private String frontendReturnUrl;

    // Endpoint to initiate the Steam login process.
    @GetMapping("/login")
    public ResponseEntity<SteamLoginResponse> login(HttpSession session) {
        try {
            String redirectUrl = steamOpenIDService.generateLoginUrl(session);
            return ResponseEntity.ok(new SteamLoginResponse(redirectUrl, null));
        } catch(Exception e) {
            return ResponseEntity.status(500)
                    .body(new SteamLoginResponse(null, "Error initiating Steam login: " + e.getMessage()));
        }
    }
    @GetMapping("/return")
    public void steamReturn(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        try {
            String steamId = steamOpenIDService.verifyResponse(request, session);
            if (steamId != null) {
                session.setAttribute("steamId", steamId);
                SteamAuthResponse authResponse = new SteamAuthResponse("true", steamId, null);
                String redirectUrl = buildRedirectUrl(authResponse);
                response.sendRedirect(redirectUrl);
            } else {
                SteamAuthResponse authResponse = new SteamAuthResponse("false", null, "Verification failed");
                String redirectUrl = buildRedirectUrl(authResponse);
                response.sendRedirect(redirectUrl);
            }
        } catch (Exception e) {
            SteamAuthResponse authResponse = new SteamAuthResponse("false", null, e.getMessage());
            String redirectUrl = buildRedirectUrl(authResponse);
            response.sendRedirect(redirectUrl);
        }
    }
    private String buildRedirectUrl(SteamAuthResponse authResponse) throws IOException {
        String baseUrl = frontendReturnUrl;
        StringBuilder redirectUrl = new StringBuilder(baseUrl);
        redirectUrl.append("?success=").append(authResponse.getSuccess());
        
        if (authResponse.getSteamID() != null) {
            redirectUrl.append("&steamID=").append(URLEncoder.encode(authResponse.getSteamID(), StandardCharsets.UTF_8.toString()));
        }
        if (authResponse.getError() != null) {
            redirectUrl.append("&error=").append(URLEncoder.encode(authResponse.getError(), StandardCharsets.UTF_8.toString()));
        }
        return redirectUrl.toString();
    }
}