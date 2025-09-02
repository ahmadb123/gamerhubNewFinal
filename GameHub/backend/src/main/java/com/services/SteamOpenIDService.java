package com.services;

import java.util.List;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;
import org.openid4java.consumer.VerificationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SteamOpenIDService {

    private static final String STEAM_OPENID_URL = "https://steamcommunity.com/openid";
    private final ConsumerManager consumerManager;

    // realm for OpenID (also used as default return_to for web)
    @Value("${steam.openid.realm}")
    private String steamRealm;

    // (optional) separate property if you want web to return to a front-end URL
    @Value("${steam.openid.returnUrl}")
    private String steamReturnUrl;

    public SteamOpenIDService() {
        this.consumerManager = new ConsumerManager();
        consumerManager.setMaxAssocAttempts(0);
    }

    /** Web login: use the realm as return_to  */
    public String generateLoginUrl(HttpSession session) throws Exception {
        return generateLoginUrl(session, steamRealm);
    }

    /** Mobile login: use the deep‑link as return_to  */
    public String generateLoginUrlForMobile(HttpSession session) throws Exception {
        // build a return_to of:
        //    http://localhost:8080/steam/return?mobile=true
        String mobileReturn = steamRealm + "?mobile=true";
        return generateLoginUrl(session, mobileReturn);
    }

    /** Shared logic to build an OpenID redirect URL */
    private String generateLoginUrl(HttpSession session, String returnTo) throws Exception {
        // discovery + association
        List<?> discoveries = consumerManager.discover(STEAM_OPENID_URL);
        DiscoveryInformation info = consumerManager.associate(discoveries);
        session.setAttribute("steam-openid-discovery", info);

        // build the redirect
        AuthRequest authReq = consumerManager.authenticate(info, returnTo);
        return authReq.getDestinationUrl(true);
    }

    /** Verify Steam’s OpenID response */
    public String verifyResponse(HttpServletRequest request, HttpSession session) throws Exception {
        ParameterList params = new ParameterList(request.getParameterMap());
        DiscoveryInformation discoveryInfo =
            (DiscoveryInformation) session.getAttribute("steam-openid-discovery");

        // Reconstruct full return URL + query string
        StringBuffer receivingURL = request.getRequestURL();
        String qs = request.getQueryString();
        if (qs != null) receivingURL.append("?").append(qs);

        // Verify
        VerificationResult verification =
            consumerManager.verify(receivingURL.toString(), params, discoveryInfo);

        // Extract SteamID
        if (verification.getVerifiedId() != null) {
            String claimed = verification.getVerifiedId().getIdentifier();
            return claimed.replace("https://steamcommunity.com/openid/id/", "");
        }
        return null;
    }
}
