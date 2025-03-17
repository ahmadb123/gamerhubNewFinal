package com.example.demo.UtilityTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.utility.StoreCodeVerifier;

public class StoreCodeVerifierTest {

    @Test
    public void testStoreAndRetrieveCodeVerifier() {
        String codeVerifier = "testCodeVerifier123";
        String tempId = StoreCodeVerifier.storeCodeVerifier(codeVerifier);
        assertNotNull(tempId, "Temporary ID should not be null");
        
        String retrieved = StoreCodeVerifier.retrieveCodeVerifier(tempId);
        assertEquals(codeVerifier, retrieved, "Retrieved code verifier should match the stored one");
        
        // Subsequent retrieval should return null since it's been removed
        String retrievedAgain = StoreCodeVerifier.retrieveCodeVerifier(tempId);
        assertNull(retrievedAgain, "Second retrieval should return null");
    }
}
