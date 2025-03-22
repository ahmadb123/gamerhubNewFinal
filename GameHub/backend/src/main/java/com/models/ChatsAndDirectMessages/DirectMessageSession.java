package com.models.ChatsAndDirectMessages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.models.UserModel.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "direct_message_sessions")
public class DirectMessageSession {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne 
    @JoinColumn(name = "user_a_id")
    private User userA;

    @OneToOne
    @JoinColumn(name = "user_b_id")
    private User userB;

    private LocalDate createdTime; // you may want to initialize this

    @JsonIgnore  // Prevents serialization of messages
    @OneToMany(mappedBy = "session")
    private List<DirectMessages> messages = new ArrayList<>();

    public DirectMessageSession() {
    }

    public DirectMessageSession(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
        this.createdTime = LocalDate.now(); // Set the current time when session is created
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public List<DirectMessages> getMessages() {
        return messages;
    }

    public void setMessages(List<DirectMessages> messages) {
        this.messages = messages;
    }
}
