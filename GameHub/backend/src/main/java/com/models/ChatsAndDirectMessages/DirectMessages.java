package com.models.ChatsAndDirectMessages;

import com.models.UserModel.User;

import jakarta.persistence.Entity;
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

    // constructor

    public DirectMessages() {
    }

    public DirectMessages(DirectMessageSession session, User sender, String content) {
        this.session = session;
        this.sender = sender;
        this.content = content;
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


}
