package com.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.SteamProfileRepository;
import com.Repository.XboxProfileRepository;
import com.Repository.UserRepository;
import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.UserModel.LinkedProfiles;
import com.models.XboxModel.XboxProfile;
import com.models.UserModel.User;

@Service
public class UserLinkedProfilesService {

    @Autowired
    private XboxProfileRepository xboxProfileRepository;
    @Autowired
    private SteamProfileRepository steamProfileRepository;
    @Autowired
    private UserRepository userRepository;

    // This class will handle the logic for managing user linked profiles
    // It will interact with the database to save, update, and retrieve linked profiles
    // for futrue work - psn
    private boolean userHasXboxProfile = false;
    private boolean userHasSteamProfile = false;

    /*
        * if user id matches the id in xbox_profiles table
        * then we can get the gamertag from the linked profiles
        * this means user has an account for xbox- 
    */

    public boolean checkIfUserHasXboxProfile(Long userId){
        // check if the user id in the xbox_profiles table->
        if(xboxProfileRepository.findByUserId(userId).isPresent()){
            userHasXboxProfile = true;
        }else{
            userHasXboxProfile = false;
        }
        return userHasXboxProfile;
    }

    /*
     * same thing for steam profiles
     */

    public boolean checkIfUserHasSteamProfile(Long userId){
        // check if the user id in the steam_profiles table->
        if(steamProfileRepository.findSteamUserByUserId(userId).isPresent()){
            userHasSteamProfile = true;
        }else{
            userHasSteamProfile = false;
        }
        return userHasSteamProfile;
    }
    // Updated method: now accepts a User object and checks for duplicates
    public void addXboxProfileToLinkedProfiles(User user) {
        if(checkIfUserHasXboxProfile(user.getId())){
            for(XboxProfile xboxProfile : user.getXboxProfiles()){
                if(xboxProfile.getUser().getId().equals(user.getId())){
                    String gamertag = xboxProfile.getXboxGamertag();
                    // Look for an existing linked profile with the same platform and gamertag
                    Optional<LinkedProfiles> existingLinkedProfile = user.getLinkedProfiles().stream()
                            .filter(lp -> lp.getPlatform().equals("Xbox") && lp.getGamertag().equals(gamertag))
                            .findFirst();
                    if(existingLinkedProfile.isPresent()){
                        // Update existing linked profile if needed
                        LinkedProfiles lp = existingLinkedProfile.get();
                        if(lp.getXuid() == null || lp.getUhs() == null) {
                            lp.setXuid(xboxProfile.getXuid());
                            lp.setUhs(xboxProfile.getUhs());
                        }
                    } else {
                        // Create a new linked profile if not already present
                        LinkedProfiles linkedProfile = new LinkedProfiles();
                        linkedProfile.setPlatform("Xbox");
                        linkedProfile.setGamertag(gamertag);
                        linkedProfile.setXuid(xboxProfile.getXuid());
                        linkedProfile.setUhs(xboxProfile.getUhs());
                        user.getLinkedProfiles().add(linkedProfile);
                    }
                }
            }
            userRepository.save(user);
        }
    }
    
    // Updated steam method to check for duplicates
    public void addSteamProfileToLinkedProfiles(User user){
        if(checkIfUserHasSteamProfile(user.getId())){
            for(SteamProfile steamProfile : user.getSteamProfiles()){
                if(steamProfile.getUser().getId().equals(user.getId())){
                    String gamertag = steamProfile.getPersonaname();
                    // Look for an existing linked profile with the same platform and gamertag
                    Optional<LinkedProfiles> existingLinkedProfile = user.getLinkedProfiles().stream()
                            .filter(lp -> lp.getPlatform().equals("Steam") && lp.getGamertag().equals(gamertag))
                            .findFirst();
                    if(existingLinkedProfile.isPresent()){
                        // Update the steamId if it's null
                        LinkedProfiles lp = existingLinkedProfile.get();
                        if(lp.getSteamId() == null) {
                            lp.setSteamId(steamProfile.getSteamid());
                        }
                    } else {
                        // Create a new linked profile if not already present
                        LinkedProfiles linkedProfile = new LinkedProfiles();
                        linkedProfile.setPlatform("Steam");
                        linkedProfile.setGamertag(gamertag);
                        linkedProfile.setSteamId(steamProfile.getSteamid());
                        user.getLinkedProfiles().add(linkedProfile);
                    }
                }
            }
            userRepository.save(user);
        }
    }
    
    /*
     * Retrieve all linked profiles for a user
     * @param userId the ID of the user
     * return list of linked profiles
     */
    public List<LinkedProfiles> getAllLinkedProfiles(Long userId){
        // find the user by id->
        return userRepository.findById(userId)
                .map(user -> {
                    addXboxProfileToLinkedProfiles(user);
                    addSteamProfileToLinkedProfiles(user);
                    return new ArrayList<>(user.getLinkedProfiles());
                })
                .orElse(new ArrayList<>()); // Return empty list if user not found
    }
}