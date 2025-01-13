package com.dto;

public class XboxProfileDTO {
    
    private String id;                
    private String gamertag;        
    private String appDisplayName;    
    private String gameDisplayName;
    private String appDisplayPicRaw;  
    private String gameDisplayPicRaw; 
    private String accountTier;       
    private int tenureLevel;          
    private int gamerscore;          

    public XboxProfileDTO() {}

    public XboxProfileDTO(String id, String gamertag, String appDisplayName, String gameDisplayName, String appDisplayPicRaw, String gameDisplayPicRaw, String accountTier, int tenureLevel, int gamerscore) {
        this.id = id;
        this.gamertag = gamertag;
        this.appDisplayName = appDisplayName;
        this.gameDisplayName = gameDisplayName;
        this.appDisplayPicRaw = appDisplayPicRaw;
        this.gameDisplayPicRaw = gameDisplayPicRaw;
        this.accountTier = accountTier;
        this.tenureLevel = tenureLevel;
        this.gamerscore = gamerscore;
    }

    // --- Getters / Setters ---
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getGamertag() {
        return gamertag;
    }
    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getAppDisplayName() {
        return appDisplayName;
    }
    public void setAppDisplayName(String appDisplayName) {
        this.appDisplayName = appDisplayName;
    }

    public String getGameDisplayName() {
        return gameDisplayName;
    }
    public void setGameDisplayName(String gameDisplayName) {
        this.gameDisplayName = gameDisplayName;
    }

    public String getAppDisplayPicRaw() {
        return appDisplayPicRaw;
    }
    public void setAppDisplayPicRaw(String appDisplayPicRaw) {
        this.appDisplayPicRaw = appDisplayPicRaw;
    }

    public String getGameDisplayPicRaw() {
        return gameDisplayPicRaw;
    }
    public void setGameDisplayPicRaw(String gameDisplayPicRaw) {
        this.gameDisplayPicRaw = gameDisplayPicRaw;
    }

    public String getAccountTier() {
        return accountTier;
    }
    public void setAccountTier(String accountTier) {
        this.accountTier = accountTier;
    }

    public int getTenureLevel() {
        return tenureLevel;
    }
    public void setTenureLevel(int tenureLevel) {
        this.tenureLevel = tenureLevel;
    }

    public int getGamerscore() {
        return gamerscore;
    }
    public void setGamerscore(int gamerscore) {
        this.gamerscore = gamerscore;
    }
}
