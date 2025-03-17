package com.models.Steam.SteamUserProfile;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.models.UserModel.User;

import jakarta.persistence.*;

/*
 * presents players response from Steam API
 * it also represents the players data in the database
 */


@Entity
@Table(name = "steam_profiles")
public class SteamProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "steam_id")
    private String steamid;

    @Column(name = "steam_name")
    private String personaname;

    private int communityvisibilitystate;
    private int profilestate;
    private String profileurl;
    private String avatar;
    private String avatarmedium;
    private String avatarfull;
    private String avatarhash;
    private long lastlogoff;
    private int personastate;
    private String realname;
    private String primaryclanid;
    private long timecreated;
    private int personastateflags;

    // empty constructor
    public SteamProfile() {
    }

    // Constructor
    public SteamProfile(String steamid, int communityvisibilitystate, int profilestate, String personaname, String profileurl,
            String avatar, String avatarmedium, String avatarfull, String avatarhash, long lastlogoff, int personastate,
            String realname, String primaryclanid, long timecreated, int personastateflags) {
        this.steamid = steamid;
        this.communityvisibilitystate = communityvisibilitystate;
        this.profilestate = profilestate;
        this.personaname = personaname;
        this.profileurl = profileurl;
        this.avatar = avatar;
        this.avatarmedium = avatarmedium;
        this.avatarfull = avatarfull;
        this.avatarhash = avatarhash;
        this.lastlogoff = lastlogoff;
        this.personastate = personastate;
        this.realname = realname;
        this.primaryclanid = primaryclanid;
        this.timecreated = timecreated;
        this.personastateflags = personastateflags;
    }

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public int getCommunityvisibilitystate() {
        return communityvisibilitystate;
    }

    public void setCommunityvisibilitystate(int communityvisibilitystate) {
        this.communityvisibilitystate = communityvisibilitystate;
    }

    public int getProfilestate() {
        return profilestate;
    }

    public void setProfilestate(int profilestate) {
        this.profilestate = profilestate;
    }

    public String getPersonaname() {
        return personaname;
    }

    public void setPersonaname(String personaname) {
        this.personaname = personaname;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarmedium() {
        return avatarmedium;
    }

    public void setAvatarmedium(String avatarmedium) {
        this.avatarmedium = avatarmedium;
    }

    public String getAvatarfull() {
        return avatarfull;
    }

    public void setAvatarfull(String avatarfull) {
        this.avatarfull = avatarfull;
    }

    public String getAvatarhash() {
        return avatarhash;
    }

    public void setAvatarhash(String avatarhash) {
        this.avatarhash = avatarhash;
    }

    public long getLastlogoff() {
        return lastlogoff;
    }

    public void setLastlogoff(long lastlogoff) {
        this.lastlogoff = lastlogoff;
    }

    public int getPersonastate() {
        return personastate;
    }

    public void setPersonastate(int personastate) {
        this.personastate = personastate;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPrimaryclanid() {
        return primaryclanid;
    }

    public void setPrimaryclanid(String primaryclanid) {
        this.primaryclanid = primaryclanid;
    }

    public long getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(long timecreated) {
        this.timecreated = timecreated;
    }

    public int getPersonastateflags() {
        return personastateflags;
    }

    public void setPersonastateflags(int personastateflags) {
        this.personastateflags = personastateflags;
    }

    // Added getter and setter for user
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
