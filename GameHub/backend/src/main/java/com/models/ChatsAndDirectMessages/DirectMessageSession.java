// package com.models.ChatsAndDirectMessages;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.models.UserModel.User;
// import jakarta.persistence.*;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// @Entity
// @Table(name = "direct_message_sessions")
// public class DirectMessageSession {
//     @Id 
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     // Use a collection to support one-to-one and group sessions.
//     @ManyToMany
//     @JoinTable(
//         name = "dm_session_participants",
//         joinColumns = @JoinColumn(name = "session_id"),
//         inverseJoinColumns = @JoinColumn(name = "user_id")
//     )
//     private Set<User> participants = new HashSet<>();

//     private LocalDate createdTime;

//     @JsonIgnore  // Prevents serialization of messages
//     @OneToMany(mappedBy = "session")
//     private List<DirectMessages> messages = new ArrayList<>();

//     public DirectMessageSession() {
//     }

//     // Convenience constructor for one-to-one sessions.
//     public DirectMessageSession(User userA, User userB) {
//         this.participants.add(userA);
//         this.participants.add(userB);
//         this.createdTime = LocalDate.now();
//     }

//     // Constructor for group sessions.
//     public DirectMessageSession(Set<User> participants) {
//         this.participants = participants;
//         this.createdTime = LocalDate.now();
//     }

//     // Helper method: returns true if this session is a group chat.
//     public boolean isGroupSession() {
//         return participants.size() > 2;
//     }

//     // Optional: For one-to-one sessions, get the two participants.
//     public List<User> getOneToOneParticipants() {
//         return new ArrayList<>(participants);
//     }

//     // Getters and setters
//     public Long getId() {
//         return id;
//     }
//     public void setId(Long id) {
//         this.id = id;
//     }
//     public Set<User> getParticipants() {
//         return participants;
//     }
//     public void setParticipants(Set<User> participants) {
//         this.participants = participants;
//     }
//     public LocalDate getCreatedTime() {
//         return createdTime;
//     }
//     public void setCreatedTime(LocalDate createdTime) {
//         this.createdTime = createdTime;
//     }
//     public List<DirectMessages> getMessages() {
//         return messages;
//     }
//     public void setMessages(List<DirectMessages> messages) {
//         this.messages = messages;
//     }
// }



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
        if(userA.getId() < userB.getId()){
            this.userA = userA;
            this.userB = userB;
        } else {
            this.userA = userB;
            this.userB = userA;
        }
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