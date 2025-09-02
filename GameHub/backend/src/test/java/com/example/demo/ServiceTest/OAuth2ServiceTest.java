package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.services.OAuth2Service;

public class OAuth2ServiceTest {

    @Test
    public void testGenerateAuthUrl() {
        OAuth2Service service = new OAuth2Service();
        String codeChallenge = "testChallenge";
        String url = service.generateAuthUrl(codeChallenge);
        assertNotNull(url, "Authentication URL should not be null");
        assertTrue(url.contains("client_id="));
        assertTrue(url.contains("code_challenge=" + codeChallenge));
    }
}
