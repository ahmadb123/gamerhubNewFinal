package com.dto;

import java.time.LocalDateTime;

import com.models.CommunityInsight.PostNews;

public class PostNewsDTO {
    private String contentText;
    private String sharedNewsId;
    private String sharedClipsId;
    private String username; // Only include username instead of full User object
    private LocalDateTime timeShared;

    // for the sharedNewsId - fields to correspond with the external api call - 
    private String name; // n`ame of the game
    private String released; // release date
    private String background_image; // background image of the game
    public PostNewsDTO(PostNews postNews) {
        this.contentText = postNews.getContentText();
        this.sharedNewsId = postNews.getSharedNewsId();
        this.sharedClipsId = postNews.getSharedClipsId();
        this.username = postNews.getUser().getUsername();
        this.timeShared = postNews.getTimeShared();
    }
    

    public PostNewsDTO(){}
    // getters and setters- >


    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getReleased() {
        return released;
    }
    
    public void setReleased(String released) {
        this.released = released;
    }
    
    public String getBackgroundImage() {
        return background_image;
    }
    
    public void setBackgroundImage(String background_image) {
        this.background_image = background_image;
    }
    
    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getSharedNewsId() {
        return sharedNewsId;
    }

    public void setSharedNewsId(String sharedNewsId) {
        this.sharedNewsId = sharedNewsId;
    }

    public String getSharedClipsId() {
        return sharedClipsId;
    }

    public void setSharedClipsId(String sharedClipsId) {
        this.sharedClipsId = sharedClipsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimeShared() {
        return timeShared;
    }

    public void setTimeShared(LocalDateTime timeShared) {
        this.timeShared = timeShared;
    }

}
