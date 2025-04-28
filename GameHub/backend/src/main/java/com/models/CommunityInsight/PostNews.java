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

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "shared_news_id")
    private String sharedNewsId;

    @Column(name = "shared_clips_id")
    private String sharedClipsId;

    @Column(name = "shared_clips_url")
    private String sharedClipsUrl;

    @Column(name = "time_shared")
    private LocalDateTime timeShared;

    public PostNews() {}

    public PostNews(User user, String contentText, String sharedNewsId,
                    String sharedClipsId, String sharedClipsUrl,
                    LocalDateTime timeShared) {
        this.user          = user;
        this.contentText   = contentText;
        this.sharedNewsId  = sharedNewsId;
        this.sharedClipsId = sharedClipsId;
        this.sharedClipsUrl= sharedClipsUrl;
        this.timeShared    = timeShared;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getContentText() { return contentText; }
    public void setContentText(String contentText) { this.contentText = contentText; }

    public String getSharedNewsId() { return sharedNewsId; }
    public void setSharedNewsId(String sharedNewsId) { this.sharedNewsId = sharedNewsId; }

    public String getSharedClipsId() { return sharedClipsId; }
    public void setSharedClipsId(String sharedClipsId) { this.sharedClipsId = sharedClipsId; }

    public String getSharedClipsUrl() { return sharedClipsUrl; }
    public void setSharedClipsUrl(String sharedClipsUrl) { this.sharedClipsUrl = sharedClipsUrl; }

    public LocalDateTime getTimeShared() { return timeShared; }
    public void setTimeShared(LocalDateTime timeShared) { this.timeShared = timeShared; }
}
