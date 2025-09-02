package com.models.XboxModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.models.UserModel.User;
import jakarta.persistence.*;

@Entity
@Table(name = "xbox_profiles")
public class XboxProfile {

    // Primary Key in our database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many XboxProfiles can reference the same User.
     * 'fetch=LAZY' is typical in JPA so we only load the User when needed.
     * 'nullable=false' means each XboxProfile must belong to some User.
     */
    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore 
    private User user;

    /**
     * The field below is specifically for storing the
     * "id" (XUID) from the Xbox API JSON, but we rename it to 'xboxId'
     * to avoid clashing with our primary key 'id'.
     */
    @Column(name = "xbox_id")
    private String xboxId;

    @Column(name = "xbox_gamertag")
    private String xboxGamertag;

    private String appDisplayName;
    private String gameDisplayName;
    private String appDisplayPicRaw;
    private String gameDisplayPicRaw;
    private String accountTier;
    private int tenureLevel;
    private int gamerscore;
    private String uhs; 
    private String xuid;
    @Column(name = "xsts", columnDefinition = "TEXT")
    private String xsts;

    public XboxProfile() {}

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getXboxId() {
        return xboxId;
    }

    public String getXboxGamertag() {
        return xboxGamertag;
    }

    public String getAppDisplayName() {
        return appDisplayName;
    }

    public String getGameDisplayName() {
        return gameDisplayName;
    }

    public String getXsts(){
        return xsts;
    }


    public String getAppDisplayPicRaw() {
        return appDisplayPicRaw;
    }

    public String getGameDisplayPicRaw() {
        return gameDisplayPicRaw;
    }

    public String getAccountTier() {
        return accountTier;
    }

    public int getTenureLevel() {
        return tenureLevel;
    }

    public int getGamerscore() {
        return gamerscore;
    }

    public String getUhs() {
        return uhs;
    }


    public String getXuid() {
        return xuid;
    }


    // --- Setters ---
    public void setUser(User user) {
        this.user = user;
    }

    public void setXboxId(String xboxId) {
        this.xboxId = xboxId;
    }

    public void setXboxGamertag(String xboxGamertag) {
        this.xboxGamertag = xboxGamertag;
    }

    public void setAppDisplayName(String appDisplayName) {
        this.appDisplayName = appDisplayName;
    }

    public void setXsts(String xsts){
        this.xsts = xsts;
    }

    public void setGameDisplayName(String gameDisplayName) {
        this.gameDisplayName = gameDisplayName;
    }

    public void setAppDisplayPicRaw(String appDisplayPicRaw) {
        this.appDisplayPicRaw = appDisplayPicRaw;
    }

    public void setGameDisplayPicRaw(String gameDisplayPicRaw) {
        this.gameDisplayPicRaw = gameDisplayPicRaw;
    }

    public void setAccountTier(String accountTier) {
        this.accountTier = accountTier;
    }

    public void setTenureLevel(int tenureLevel) {
        this.tenureLevel = tenureLevel;
    }

    public void setGamerscore(int gamerscore) {
        this.gamerscore = gamerscore;
    }

    public void setUhs(String uhs) {
        this.uhs = uhs;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
