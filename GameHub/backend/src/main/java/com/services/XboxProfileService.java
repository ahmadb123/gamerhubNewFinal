package com.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.XboxProfile;
import org.springframework.stereotype.Service;

@Service
public class XboxProfileService {

    // Parses the response body into an XboxProfile object
    public XboxProfile parseProfile(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // Extract profile details
        JsonNode profileNode = rootNode.path("profileUsers").get(0).path("settings");
        XboxProfile profile = new XboxProfile();

        profile.setGamertag(getValueFromSettings(profileNode, "Gamertag"));
        profile.setGameDisplayName(getValueFromSettings(profileNode, "GameDisplayName"));
        profile.setAppDisplayPicRaw(getValueFromSettings(profileNode, "AppDisplayPicRaw"));

        return profile;
    }

    // Helper method to get the value for a specific setting
    private String getValueFromSettings(JsonNode settingsNode, String id) {
        for (JsonNode setting : settingsNode) {
            if (setting.path("id").asText().equals(id)) {
                return setting.path("value").asText();
            }
        }
        return null; // Return null if the setting ID is not found
    }
}
