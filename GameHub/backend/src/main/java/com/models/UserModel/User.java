/*
 * User Model - 
 * for regestering and login
 */
package com.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import com.models.XboxModel.XboxProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
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
    private String gamertag;
    private String accountTier;
    private String appDisplayName;
    private String appDisplayPicRaw;
    private String gameDisplayName;
    private String gameDisplayPicRaw;
    private Integer gamerscore;
    private String xboxGamertag;
    private String xboxId;
    private Integer tenureLevel;
    private String uhs;
    private String xuid;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XboxProfile> xboxProfiles = new ArrayList<>();
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<PSNProfile> psnProfiles = new ArrayList<>();
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<SteamProfile> steamProfiles = new ArrayList<>();

    
    // Getters
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
    public String getGamertag() { return gamertag; }
    public String getAccountTier() { return accountTier; }
    public String getAppDisplayName() { return appDisplayName; }
    public String getAppDisplayPicRaw() { return appDisplayPicRaw; }
    public String getGameDisplayName() { return gameDisplayName; }
    public String getGameDisplayPicRaw() { return gameDisplayPicRaw; }
    public Integer getGamerscore() { return gamerscore; }
    public String getXboxGamertag() { return xboxGamertag; }
    public String getXboxId() { return xboxId; }
    public Integer getTenureLevel() { return tenureLevel; }
    public String getUhs() { return uhs; }
    public String getXuid() { return xuid; }

    public Boolean getIsAuthorized() {
        return isAuthorized;
    }
    
    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    // Setters

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
    public void setGamertag(String gamertag) { this.gamertag = gamertag; }
    public void setAccountTier(String accountTier) { this.accountTier = accountTier; }
    public void setAppDisplayName(String appDisplayName) { this.appDisplayName = appDisplayName; }
    public void setAppDisplayPicRaw(String appDisplayPicRaw) { this.appDisplayPicRaw = appDisplayPicRaw; }
    public void setGameDisplayName(String gameDisplayName) { this.gameDisplayName = gameDisplayName; }
    public void setGameDisplayPicRaw(String gameDisplayPicRaw) { this.gameDisplayPicRaw = gameDisplayPicRaw; }
    public void setGamerscore(Integer gamerscore) { this.gamerscore = gamerscore; }
    public void setXboxGamertag(String xboxGamertag) { this.xboxGamertag = xboxGamertag; }
    public void setXboxId(String xboxId) { this.xboxId = xboxId; }
    public void setTenureLevel(Integer tenureLevel) { this.tenureLevel = tenureLevel; }
    public void setUhs(String uhs) { this.uhs = uhs; }
    public void setXuid(String xuid) { this.xuid = xuid; }

    public List<XboxProfile> getXboxProfiles() { return xboxProfiles; }
    public void setXboxProfiles(List<XboxProfile> xboxProfiles) { this.xboxProfiles = xboxProfiles; }


}
