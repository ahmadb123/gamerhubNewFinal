package com.example.demo.ServiceTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import com.services.TokenService;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class TokenServiceTest {

    private TokenService tokenService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        tokenService = new TokenService();
        restTemplate = mock(RestTemplate.class);
        try {
            java.lang.reflect.Field field = TokenService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(tokenService, restTemplate);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testExchangeAccessTokenForXboxTokens() throws Exception {
        String dummyAccessToken = "dummyAccessToken";
        // Sample JSON for XBL token exchange.
        String xblResponseJson = "{ \"Token\": \"xblToken123\", \"DisplayClaims\": { \"xui\": [ { \"uhs\": \"userhash123\" } ] } }";
        ResponseEntity<String> xblResponse = ResponseEntity.ok(xblResponseJson);
        // Sample JSON for XSTS token exchange.
        String xstsResponseJson = "{ \"Token\": \"xstsToken456\" }";
        ResponseEntity<String> xstsResponse = ResponseEntity.ok(xstsResponseJson);
        
        // Chain responses: first call returns XBL, second returns XSTS.
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
            .thenReturn(xblResponse)
            .thenReturn(xstsResponse);

        tokenService.exchangeAccessTokenForXboxTokens(dummyAccessToken);
        assertEquals("xblToken123", tokenService.getXblToken());
        assertEquals("userhash123", tokenService.getUhs());
        assertEquals("xstsToken456", tokenService.getXstsToken());
    }
}
