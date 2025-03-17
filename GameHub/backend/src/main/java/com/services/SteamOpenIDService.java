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

    // Inject the realm and the return URL from configuration
    @Value("${steam.openid.realm}")
    private String steamRealm;

    @Value("${steam.openid.returnUrl}")
    private String steamReturnUrl; 

    public SteamOpenIDService() {
        this.consumerManager = new ConsumerManager();
        // Operate in stateless mode
        consumerManager.setMaxAssocAttempts(0);
    }

    public String generateLoginUrl(HttpSession session) throws Exception {
        // Discover Steam’s OpenID endpoint
        List discoveries = consumerManager.discover(STEAM_OPENID_URL);
        DiscoveryInformation discoveryInfo = consumerManager.associate(discoveries);
        
        // Store discovery information in session for later verification
        session.setAttribute("steam-openid-discovery", discoveryInfo);
        
        // Build the authentication request using your configured realm
        AuthRequest authReq = consumerManager.authenticate(discoveryInfo, steamRealm);
        
        // Remove explicit setting of assoc_type because AuthRequest does not support setParameter
        
        // Return the complete redirect URL
        return authReq.getDestinationUrl(true);
    }
    
    public String verifyResponse(HttpServletRequest request, HttpSession session) throws Exception {
        // 1) Get parameters from the callback request
        ParameterList paramList = new ParameterList(request.getParameterMap());

        // 2) Retrieve original discovery info from session
        DiscoveryInformation discoveryInfo = (DiscoveryInformation) session.getAttribute("steam-openid-discovery");

        // 3) Build the receiving URL (the current URL)
        StringBuffer receivingURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString != null) {
            receivingURL.append("?").append(queryString);
        }

        // 4) Verify the OpenID response
        VerificationResult verification = consumerManager.verify(receivingURL.toString(), paramList, discoveryInfo);

        // 5) If verified, extract the claimed ID (SteamID)
        if (verification.getVerifiedId() != null) {
            String claimedId = verification.getVerifiedId().getIdentifier();
            // Convert "https://steamcommunity.com/openid/id/76561198012345678" to "76561198012345678"
            String steamId = claimedId.replace("https://steamcommunity.com/openid/id/", "");
            return steamId;
        } else {
            return null;
        }
    }
}