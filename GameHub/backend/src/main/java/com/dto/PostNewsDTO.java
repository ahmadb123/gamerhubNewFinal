package com.dto;

import java.time.LocalDateTime;

import com.models.CommunityInsight.PostNews;

public class PostNewsDTO {
    private String contentText;
    private String sharedNewsId;
    private String sharedClipsUrl;
    private String username;
    private LocalDateTime timeShared;
    private long likesCount;
    private long commentsCount;
    // for the sharedNewsId - fields to correspond with the external api call - 
    private String name; // name of the game
    private String released; // release date
    private String background_image; // background image of the game
    private Long id;

    public PostNewsDTO(PostNews postNews) {
        this.contentText = postNews.getContentText();
        this.sharedNewsId = postNews.getSharedNewsId();
        this.sharedClipsUrl = postNews.getSharedClipsUrl();
        this.username = postNews.getUser().getUsername();
        this.timeShared = postNews.getTimeShared();
        this.id = postNews.getId();
    }
    

    public PostNewsDTO(){}
    // getters and setters- >

    public String getSharedClipsUrl() {
        return sharedClipsUrl;
    }

    public void setSharedClipsUrl(String sharedXboxXuid) {
        this.sharedClipsUrl = sharedXboxXuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
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

