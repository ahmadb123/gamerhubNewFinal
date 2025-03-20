/*
 * User Model - 
 * for regestering and login
 */
package com.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import com.models.FriendsModel.Friends;
import com.models.Steam.SteamUserProfile.SteamProfile;
import com.models.UserSavedGames.MyGames;
import com.models.XboxModel.XboxProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAuthorized = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friends> initiatedFriendRequest = new ArrayList<>();
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friends> receivedFriendRequest = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XboxProfile> xboxProfiles = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<MyGames> myGames;
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<PSNProfile> psnProfiles = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SteamProfile> steamProfiles = new ArrayList<>();

    // describe user's all connected profiles- xbox, steam, psn including their gamertgs-?>
    // use linked list?

    @ElementCollection
    @CollectionTable(name = "user_linked_profiles", joinColumns = @JoinColumn(name = "user_id"))
    private List<LinkedProfiles> linkedProfiles = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Boolean getIsAuthorized() {
        return isAuthorized;
    }
    
    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public List<LinkedProfiles> getLinkedProfiles() {
        return linkedProfiles;
    }

    public void setLinkedProfiles(List<LinkedProfiles> linkedProfiles) {
        this.linkedProfiles = linkedProfiles;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Friends> getFriendshipsInitiated() {
        return initiatedFriendRequest;
    }
    public void setFriendshipsInitiated(List<Friends> friendshipsInitiated) {
        this.initiatedFriendRequest = friendshipsInitiated;
    }
    
    public List<Friends> getFriendshipsReceived() {
        return receivedFriendRequest;
    }
    public void setFriendshipsReceived(List<Friends> friendshipsReceived) {
        this.receivedFriendRequest = friendshipsReceived;
    }
    

    public List<XboxProfile> getXboxProfiles() { return xboxProfiles; }
    public void setXboxProfiles(List<XboxProfile> xboxProfiles) { this.xboxProfiles = xboxProfiles; }

    public List<MyGames> getMyGames() {
        return myGames;
    }

    public void setMyGames(List<MyGames> myGames) {
        this.myGames = myGames;
    }

    public List<SteamProfile> getSteamProfiles() {
        return steamProfiles;
    }

    public void setSteamProfiles(List<SteamProfile> steamProfiles) {
        this.steamProfiles = steamProfiles;
    }
    
}
