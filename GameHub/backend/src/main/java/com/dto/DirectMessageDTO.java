package com.dto;

import java.time.LocalDateTime;

import com.models.ChatsAndDirectMessages.MessageStatus;

public class DirectMessageDTO {
    private Long id;
    private String senderUsername;
    private String content;
    private MessageStatus messageStatus;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    public DirectMessageDTO() {}

    public DirectMessageDTO(Long id, String senderUsername, String content, MessageStatus messageStatus, LocalDateTime deliveredAt, LocalDateTime readAt) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.content = content;
        this.messageStatus = messageStatus;
        this.deliveredAt = deliveredAt;
        this.readAt = readAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
