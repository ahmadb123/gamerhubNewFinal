package com.services;

import java.util.LinkedList;

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

    // since we are using linked list we should store the data for each platform using linkelist->
    private LinkedList<LinkedProfiles> linkedProfiles = new LinkedList<>(); // stores all platform and gamertags
    // xbox platform-
    private LinkedList<XboxProfile> xboxProfiles = new LinkedList<>();
    // steam platform-
    private LinkedList<SteamProfile> steamProfiles = new LinkedList<>();
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

    // Helper method to check if a linked profile exists
    private boolean isAlreadyLinked(User user, String platform, String gamertag) {
        return user.getLinkedProfiles().stream()
                .anyMatch(lp -> lp.getPlatform().equals(platform) && lp.getGamertag().equals(gamertag));
    }
    
    // Updated method: now accepts a User object and checks for duplicates
    public void addXboxProfileToLinkedProfiles(User user) {
        if(checkIfUserHasXboxProfile(user.getId())){
            for(XboxProfile xboxProfile : user.getXboxProfiles()){
                if(xboxProfile.getUser().getId().equals(user.getId())){
                    String gamertag = xboxProfile.getXboxGamertag();
                    if(!isAlreadyLinked(user, "Xbox", gamertag)){
                        LinkedProfiles linkedProfile = new LinkedProfiles();
                        linkedProfile.setPlatform("Xbox");
                        linkedProfile.setGamertag(gamertag);
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
                    if(!isAlreadyLinked(user, "Steam", gamertag)){
                        LinkedProfiles linkedProfile = new LinkedProfiles();
                        linkedProfile.setPlatform("Steam");
                        linkedProfile.setGamertag(gamertag);
                        user.getLinkedProfiles().add(linkedProfile);
                    }
                }
            }
            userRepository.save(user);
        }
    }
}