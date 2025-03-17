package com.example.demo.UtilityTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.utility.JWT;
import java.lang.reflect.Field;

public class JWTTest {

    private JWT jwt;
    
    @BeforeEach
    public void setup() throws Exception {
        jwt = new JWT();
        // Set a secret key that is at least 256 bits (32 bytes) long.
        // Here, "abcdefghijklmnopqrstuvwxyz012345" is 32 characters (assuming ASCII, 32 bytes)
        // and its Base64 encoding is used.
        Field secretField = JWT.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwt, "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4MDEyMzQ1Njc="); 
        // Set expiration to 1 hour.
        Field expirationField = JWT.class.getDeclaredField("expirationTimeInMillis");
        expirationField.setAccessible(true);
        expirationField.set(jwt, 3600000L);
        
        // Initialize the secret key.
        jwt.afterPropertiesSet();
    }
    
    @Test
    public void testGenerateAndExtractToken() {
        String token = jwt.generateToken(123L, "testuser");
        assertNotNull(token, "Generated token should not be null");
        
        String extractedUsername = jwt.extractUsername(token);
        Long extractedUserId = jwt.extractUserId(token);
        
        assertEquals("testuser", extractedUsername, "Extracted username should match");
        assertEquals(123L, extractedUserId, "Extracted userId should match");
    }
    
    @Test
    public void testGenerateGuestToken() {
        String guestToken = jwt.generateGuestToken();
        assertNotNull(guestToken, "Generated guest token should not be null");
        String subject = jwt.extractUsername(guestToken);
        assertEquals("guest", subject, "Guest token subject should be 'guest'");
    }
}
