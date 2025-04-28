package com.models.Discord;

import com.models.UserModel.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "discord_profiles")
public class DiscordUserProfile {
// Discord user profile class 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    // Unique Discord user ID (returned by the Discord API)
    private String discordId;

    // Discord username (for example, "JohnDoe#1234")
    private String discordUsername;
    
    // URL for the user's avatar on Discord
    private String avatarUrl;
    
    // Associate the Discord profile with your User
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public DiscordUserProfile() {
    }

    public DiscordUserProfile(String discordId, String discordUsername, String avatarUrl, User user) {
        this.discordId = discordId;
        this.discordUsername = discordUsername;
        this.avatarUrl = avatarUrl;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public String getDiscordId() {
        return discordId;
    }
    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
    public String getDiscordUsername() {
        return discordUsername;
    }
    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

