
package com.models.ChatsAndDirectMessages;

import java.time.LocalDateTime;

import com.models.UserModel.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * represents the message within each session...;
 */
@Entity
@Table(name = "direct_messages")
public class DirectMessages {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // session id

    // This message belongs to a specific DirectMessageSession
    @ManyToOne
    @JoinColumn(name = "session_id")
    private DirectMessageSession session;

    // The user who actually sent this message
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    // text/content->
    private String content;

    // track read/delirved status
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus = MessageStatus.SENT;

    private LocalDateTime deliveredAt;

    private LocalDateTime readAt; 
    // constructor

    public DirectMessages() {
    }

    public DirectMessages(DirectMessageSession session, User sender, String content, MessageStatus messageStatus) {
        this.session = session;
        this.sender = sender;
        this.content = content;
        this.messageStatus = messageStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DirectMessageSession getSession() {
        return session;
    }

    public void setSession(DirectMessageSession session) {
        this.session = session;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
