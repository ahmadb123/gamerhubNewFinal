package com.models.Steam.SteamUserProfile;

public class UserOwnedAndPlayedGames {
    private int appid;
    private String name; // game name
    private int playtime_forever; // playtime in minutes
    private String img_icon_url; // image icon url
    private boolean has_community_visible_stats;
    private int playtime_windows_forever;
    private int playtime_mac_forever;
    private int playtime_linux_forever;
    private int playtime_deck_forever;
    private int rtime_last_played;
    private int playtime_disconnected;

    // empty constructor
    public UserOwnedAndPlayedGames() {
    }

    // setters and getters - 
    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaytime_forever() {
        return playtime_forever;
    }

    public void setPlaytime_forever(int playtime_forever) {
        this.playtime_forever = playtime_forever;
    }

    public String getImg_icon_url() {
        return img_icon_url;
    }

    public void setImg_icon_url(String img_icon_url) {
        this.img_icon_url = img_icon_url;
    }

    public boolean isHas_community_visible_stats() {
        return has_community_visible_stats;
    }

    public void setHas_community_visible_stats(boolean has_community_visible_stats) {
        this.has_community_visible_stats = has_community_visible_stats;
    }

    public int getPlaytime_windows_forever() {
        return playtime_windows_forever;
    }

    public void setPlaytime_windows_forever(int playtime_windows_forever) {
        this.playtime_windows_forever = playtime_windows_forever;
    }

    public int getPlaytime_mac_forever() {
        return playtime_mac_forever;
    }

    public void setPlaytime_mac_forever(int playtime_mac_forever) {
        this.playtime_mac_forever = playtime_mac_forever;
    }

    public int getPlaytime_linux_forever() {
        return playtime_linux_forever;
    }

    public void setPlaytime_linux_forever(int playtime_linux_forever) {
        this.playtime_linux_forever = playtime_linux_forever;
    }

    public int getPlaytime_deck_forever() {
        return playtime_deck_forever;
    }

    public void setPlaytime_deck_forever(int playtime_deck_forever) {
        this.playtime_deck_forever = playtime_deck_forever;
    }

    public int getRtime_last_played() {
        return rtime_last_played;
    }

    public void setRtime_last_played(int rtime_last_played) {
        this.rtime_last_played = rtime_last_played;
    }

    public int getPlaytime_disconnected() {
        return playtime_disconnected;
    }

    public void setPlaytime_disconnected(int playtime_disconnected) {
        this.playtime_disconnected = playtime_disconnected;
    }
}
