package com.services;

import com.Repository.XboxProfileRepository;
import com.dto.XboxProfileDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.UserModel.User;
import com.models.XboxModel.XboxProfile;
import com.utility.XboxProfileMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XboxProfileService {
    @Autowired
    private XboxProfileRepository saveXboxProfile;
    @Autowired
    private TokenService tokenService;
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
        dto.setXuid(tokenService.getXuid());
        dto.setUhs(tokenService.getUhs());
        dto.setXsts(tokenService.getXstsToken());
        return dto;
    }   

    // save profile - 
    public XboxProfile saveProfile(XboxProfileDTO dto, User user){
       // check if xbox profile already exists in user to avoid duplications
       System.out.println("User from DB: id=" + user.getId() + ", username=" + user.getUsername());
  
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
            profile.setXuid(dto.getXuid());
            profile.setUhs(dto.getUhs());
            profile.setXsts(dto.getXsts());
        }else{
            // Create a new profile
            profile = XboxProfileMapper.toEntity(dto, user);
        }
        //save profile 
        return saveXboxProfile.save(profile);
    }

    // return searhed user profile - 
    public XboxProfileDTO getSearchedProfileData(String username){
        Optional<XboxProfile> user = saveXboxProfile.findXboxUserByUsername(username);
        // only return the first user found - 
        System.out.println("Result of findXboxUserByUsername for " + username + ": " + user);
        if(user.isEmpty()){
            System.out.println("User not found");
            return null;
        }
        XboxProfile profile = user.get();
        XboxProfileDTO dto = new XboxProfileDTO(
             // The user’s database username:
            profile.getUser().getUsername(),
            profile.getXboxId(),
            profile.getXboxGamertag(),
            profile.getAppDisplayName(),
            profile.getGameDisplayName(),
            profile.getAppDisplayPicRaw(),
            profile.getGameDisplayPicRaw(),
            profile.getAccountTier(),
            profile.getTenureLevel(),
            profile.getGamerscore(),
            profile.getUhs(), 
            profile.getXuid(),
            profile.getXsts()
        );
        return dto;
    }

    // return all linked accounts
    public List<XboxProfileDTO> getAllLinkedAccounts(Long id){
        List<XboxProfile> profiles = saveXboxProfile.findAllByUserId(id);
        List<XboxProfileDTO> dtos = new ArrayList<>();
        for(XboxProfile profile : profiles){
            XboxProfileDTO dto = new XboxProfileDTO(
                profile.getUser().getUsername(),
                profile.getXboxId(),
                profile.getXboxGamertag(),
                profile.getAppDisplayName(),
                profile.getGameDisplayName(),
                profile.getAppDisplayPicRaw(),
                profile.getGameDisplayPicRaw(),
                profile.getAccountTier(),
                profile.getTenureLevel(),
                profile.getGamerscore(),
                profile.getUhs(), 
                profile.getXuid(),
                profile.getXsts()
            );
            dtos.add(dto);
        }
        return dtos;
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
