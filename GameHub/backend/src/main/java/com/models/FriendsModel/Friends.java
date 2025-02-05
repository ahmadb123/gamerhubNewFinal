package com.models.FriendsModel;

import java.time.LocalDateTime;

import com.models.UserModel.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "friendships")
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who initiated the friend request
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
     // The user who is being added as a friend
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @JoinColumn(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // construction
    public Friends() {}

    public Friends(User user, User friend, String status, LocalDateTime createdAt) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public User getFriend() {
        return friend;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
