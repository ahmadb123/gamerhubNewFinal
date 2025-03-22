
package com.dto;

import java.time.LocalDate;
import com.models.ChatsAndDirectMessages.DirectMessageSession;

public class DirectMessageSessionDTO {
    private Long id;
    private Long userAId;
    private String userAUsername;
    private Long userBId;
    private String userBUsername;
    private LocalDate createdTime;

    public DirectMessageSessionDTO() {}

    public DirectMessageSessionDTO(Long id, Long userAId, String userAUsername, Long userBId, String userBUsername, LocalDate createdTime) {
        this.id = id;
        this.userAId = userAId;
        this.userAUsername = userAUsername;
        this.userBId = userBId;
        this.userBUsername = userBUsername;
        this.createdTime = createdTime;
    }

    // Static method to convert an entity to DTO
    public static DirectMessageSessionDTO fromEntity(DirectMessageSession session) {
        return new DirectMessageSessionDTO(
            session.getId(),
            session.getUserA().getId(),
            session.getUserA().getUsername(),
            session.getUserB().getId(),
            session.getUserB().getUsername(),
            session.getCreatedTime()
        );
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserAId() {
        return userAId;
    }
    public void setUserAId(Long userAId) {
        this.userAId = userAId;
    }
    public String getUserAUsername() {
        return userAUsername;
    }
    public void setUserAUsername(String userAUsername) {
        this.userAUsername = userAUsername;
    }
    public Long getUserBId() {
        return userBId;
    }
    public void setUserBId(Long userBId) {
        this.userBId = userBId;
    }
    public String getUserBUsername() {
        return userBUsername;
    }
    public void setUserBUsername(String userBUsername) {
        this.userBUsername = userBUsername;
    }
    public LocalDate getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }
}
