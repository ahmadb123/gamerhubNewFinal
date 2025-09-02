package com.controllers.SteamController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    // your existing web frontend callback (React‑Router)
    @Value("${steam.openid.returnUrl}")
    private String frontendReturnUrl;  // e.g. http://localhost:3000/platform-select

    // your app’s custom scheme for deep‑links
    private static final String MOBILE_DEEP_LINK = "my-new-project://auth";

    /**  
     * Mobile: return JSON { redirectUrl, error } for RN  
     */
    @GetMapping("/login/mobile")
    public ResponseEntity<SteamLoginResponse> loginMobile(HttpSession session) {
        try {
            // generateLoginUrlForMobile just appends ?mobile=true to your HTTP callback
            String redirectUrl = steamOpenIDService.generateLoginUrlForMobile(session);
            return ResponseEntity.ok(new SteamLoginResponse(redirectUrl, null));
        } catch(Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SteamLoginResponse(null, "Error initiating Steam login: " + e.getMessage()));
        }
    }

    /**
     * Handle **all** callbacks here—web and mobile both hit /steam/return
     */
    @GetMapping("/return")
    public void steamReturn(HttpServletRequest req,
                            HttpSession session,
                            HttpServletResponse resp) throws IOException {
        boolean mobile = "true".equals(req.getParameter("mobile"));

        String steamId = null;
        try {
            steamId = steamOpenIDService.verifyResponse(req, session);
        } catch(Exception ignored) {}

        if (steamId != null) {
            if (mobile) {
                // deep‑link your app
                resp.sendRedirect(MOBILE_DEEP_LINK + "?steamID=" + steamId);
            } else {
                // normal browser flow
                SteamAuthResponse auth = new SteamAuthResponse("true", steamId, null);
                resp.sendRedirect(buildRedirectUrl(auth));
            }
        } else {
            if (mobile) {
                resp.sendRedirect(MOBILE_DEEP_LINK + "?error=verification_failed");
            } else {
                SteamAuthResponse auth = new SteamAuthResponse("false", null, "Verification failed");
                resp.sendRedirect(buildRedirectUrl(auth));
            }
        }
    }

    private String buildRedirectUrl(SteamAuthResponse auth) throws IOException {
        StringBuilder url = new StringBuilder(frontendReturnUrl)
            .append("?success=").append(auth.getSuccess());
        if (auth.getSteamID()!=null) url.append("&steamID=").append(
            URLEncoder.encode(auth.getSteamID(), StandardCharsets.UTF_8));
        if (auth.getError()  !=null) url.append("&error=").append(
            URLEncoder.encode(auth.getError(),   StandardCharsets.UTF_8));
        return url.toString();
    }
}
