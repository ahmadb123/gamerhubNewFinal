package com.dto;

import java.util.List;
import com.models.XboxModel.XboxProfile;

/**
 * A simple DTO to hold user info plus their Xbox profiles.
 */
public class FriendWithXboxDTO {
    private Long userId;
    private String username;
    private String email;            // if you want email or other fields
    private List<XboxProfile> xboxProfiles;

    public FriendWithXboxDTO() {}

    // Getters and setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<XboxProfile> getXboxProfiles() {
        return xboxProfiles;
    }
    public void setXboxProfiles(List<XboxProfile> xboxProfiles) {
        this.xboxProfiles = xboxProfiles;
    }
}
