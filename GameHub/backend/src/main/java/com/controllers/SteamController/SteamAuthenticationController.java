package com.controllers.SteamController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.services.SteamOpenIDService;

@RestController
@RequestMapping("/steam")
public class SteamAuthenticationController {

    @Autowired
    private SteamOpenIDService steamOpenIDService;

    // Inject the frontend return URL from properties
    @Value("${steam.openid.returnUrl}")
    private String frontendReturnUrl;

    // Endpoint to initiate the Steam login process.
    @GetMapping("/login")
    public ResponseEntity<?> login(HttpSession session) {
        try {
            // Generate the login URL using the SteamOpenIDService.
            String redirectUrl = steamOpenIDService.generateLoginUrl(session);
            // Return the URL in a structured response model.
            // Alternatively, you could perform a redirect immediately if preferred.
            return ResponseEntity.ok().body(Collections.singletonMap("redirectURL", redirectUrl));
        } catch(Exception e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Error initiating Steam login: " + e.getMessage()));
        }
    }

    // Endpoint to handle the return/callback from Steam.
    @GetMapping("/return")
    public void steamReturn(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        try {
            String steamId = steamOpenIDService.verifyResponse(request, session);
            if (steamId != null) {
                // Optionally, store the SteamID in session for later use
                session.setAttribute("steamId", steamId);
                // Redirect to the frontend URL (which was externalized in your properties)
                
                response.sendRedirect(frontendReturnUrl);
            } else {
                response.sendRedirect("http://localhost:3000/login?error=verification_failed");
            }
        } catch (Exception e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect("http://localhost:3000/login?error=" + errorMsg);
        }
    }
}
