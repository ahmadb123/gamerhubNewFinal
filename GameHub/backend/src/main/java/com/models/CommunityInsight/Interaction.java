package com.models.CommunityInsight;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.models.UserModel.User;

@Entity
@Table(name = "interactions")
public class Interaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "post_id", nullable = false)
    private PostNews post;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private InteractionType type;  // LIKE or COMMENT

    // only used when type == COMMENT
    @Column(columnDefinition = "TEXT")
    private String commentText;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Interaction() {}

    public Interaction(PostNews post, User user, InteractionType type, String commentText) {
        this.post = post;
        this.user = user;
        this.type = type;
        this.commentText = commentText;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public PostNews getPost() { return post; }

    public void setPost(PostNews post) { this.post = post; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public InteractionType getType() { return type; }

    public void setType(InteractionType type) { this.type = type; }

    public String getCommentText() { return commentText; }

    public void setCommentText(String commentText) { this.commentText = commentText; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public enum InteractionType {
        LIKE,
        COMMENT
    }
}
