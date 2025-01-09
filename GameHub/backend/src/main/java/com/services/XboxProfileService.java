package com.services;

import com.Repository.XboxProfileRepository;
import com.dto.XboxProfileDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.UserModel.User;
import com.models.XboxModel.XboxProfile;
import com.utility.XboxProfileMapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XboxProfileService {
    @Autowired
    private XboxProfileRepository saveXboxProfile;
    // Parses the response into DTO- 
    public XboxProfileDTO parseProfileJson(String responseBody) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);

        JsonNode profileUser = rootNode.path("profileUsers").get(0);
        JsonNode settingsNode = profileUser.path("settings");
        String xuid = profileUser.path("id").asText();

        // build DTO - 
        XboxProfileDTO dto = new XboxProfileDTO();
        dto.setId(xuid);
        dto.setGamertag(getValue(settingsNode, "Gamertag"));
        dto.setGameDisplayName(getValue(settingsNode, "GameDisplayName"));
        dto.setAppDisplayPicRaw(getValue(settingsNode, "AppDisplayPicRaw"));
        dto.setGameDisplayPicRaw(getValue(settingsNode, "GameDisplayPicRaw"));
        dto.setAccountTier(getValue(settingsNode, "AccountTier"));
        dto.setTenureLevel(parseIntSafe(getValue(settingsNode, "TenureLevel")));
        dto.setGamerscore(parseIntSafe(getValue(settingsNode, "Gamerscore")));
        return dto;
    }   

    // save profile - 
    public XboxProfile saveProfile(XboxProfileDTO dto, User user){
       // check if xbox profile already exists in user to avoid duplications  
       Optional<XboxProfile> existingProfile = saveXboxProfile.findByUserIdAndGamertag(user.getId(), dto.getGamertag());
       if(existingProfile == null) {
        System.out.println("FAILEDD");
       }
        XboxProfile profile;
        if(existingProfile.isPresent()){
            // update exisiting profile - 
            profile = existingProfile.get();
            profile.setAccountTier(dto.getAccountTier());
            profile.setAppDisplayName(dto.getAppDisplayName());
            profile.setAppDisplayPicRaw(dto.getAppDisplayPicRaw());
            profile.setGameDisplayName(dto.getGameDisplayName());
            profile.setGameDisplayPicRaw(dto.getGameDisplayPicRaw());
            profile.setGamerscore(dto.getGamerscore());
            profile.setTenureLevel(dto.getTenureLevel());
            profile.setXboxGamertag(dto.getGamertag());
            profile.setXboxId(dto.getId());
        }else{
            // Create a new profile
            profile = XboxProfileMapper.toEntity(dto, user);
        }
        //save profile 
        return saveXboxProfile.save(profile);
    }

    // helpers-  
    private String getValue(JsonNode settings, String settingId) {
        for (JsonNode setting : settings) {
            if (setting.path("id").asText().equals(settingId)) {
                return setting.path("value").asText();
            }
        }
        return null;
    }

    private int parseIntSafe(String val) {
        if (val == null) return 0;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
