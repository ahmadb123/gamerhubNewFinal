
package com.utility;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StoreCodeVerifier {
    
    // In-memory store: tempId -> codeVerifier
    private static final Map<String, String> store = new ConcurrentHashMap<>();

    // Store the code verifier and return a generated temporary identifier
    public static String storeCodeVerifier(String codeVerifier) {
        String tempId = UUID.randomUUID().toString();
        store.put(tempId, codeVerifier);
        return tempId;
    }

    // Retrieve and remove the code verifier by its temporary identifier
    public static String retrieveCodeVerifier(String tempId) {
        return store.remove(tempId);
    }
}
