package com.models.CommunityInsight;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.models.UserModel.User;

@Entity
@Table(name = "PostNews")
public class PostNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the users table
    private User user;

    private String contentText;

    private String sharedNewsId; // ID from the external news API

    private String sharedClipsId; // ID of the shared clip, if any

    private LocalDateTime timeShared;

    // Constructors
    public PostNews() {}

    public PostNews(User user, String contentText, String sharedNewsId, String sharedClipsId, LocalDateTime timeShared) {
        this.user = user;
        this.contentText = contentText;
        this.sharedNewsId = sharedNewsId;
        this.sharedClipsId = sharedClipsId;
        this.timeShared = timeShared;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getTimeShared() {
        return timeShared;
    }

    public void setTimeShared(LocalDateTime timeShared) {
        this.timeShared = timeShared;
    }
}
