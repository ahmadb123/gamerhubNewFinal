package com.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.SteamProfileRepository;
import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.Steam.SteamUserProfile.UserInfo;
import com.models.UserModel.User;

@Service
public class SteamProfileService {
    @Autowired
    private SteamProfileRepository steamProfileRepository;
    @Autowired
    private SteamUserService userService;
    // save profile to database - 
    public SteamProfile saveProfile(SteamProfile steamProfile, User user){
        // check if user already exists in database
        System.out.println("User from DB: id=" +user.getId() + ", username=" + user.getUsername());
        Optional<SteamProfile> existingProfile = steamProfileRepository.findUserIdAndSteamName(user.getId(), steamProfile.getPersonaname());

        if(!existingProfile.isPresent()){
            // get user info from steam API
            UserInfo userData = userService.getUserInfo(steamProfile.getSteamid());
            if(userData != null && userData.getPlayers() != null && !userData.getPlayers().isEmpty()){
                SteamProfile newProfile = userData.getPlayers().get(0);
                // attach the user
                newProfile.setUser(user);
                // save the new profile
                return steamProfileRepository.save(newProfile);
            }
        }
        return existingProfile.orElse(null);
    }
}
