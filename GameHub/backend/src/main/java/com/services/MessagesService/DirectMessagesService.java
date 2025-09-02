package com.services.MessagesService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.Repository.MessagesRepository.DirectMessageRepository;
import com.Repository.MessagesRepository.DirectMessageSessionRepository;
import com.models.ChatsAndDirectMessages.DirectMessageSession;
import com.models.ChatsAndDirectMessages.DirectMessages;
import com.models.ChatsAndDirectMessages.MessageStatus;
import com.models.UserModel.User;

import jakarta.transaction.Transactional;

@Service
public class DirectMessagesService {
    @Autowired
    private DirectMessageRepository directMessageRepository;
    @Autowired
    private DirectMessageSessionRepository directMessageSessionRepository;
    private boolean sessionExists = false;

    public boolean checkIfSessionExists(User userOne, User userTwo){
        if(directMessageSessionRepository.findSessionBetweenUsers(userOne.getId(), userTwo.getId()).isPresent()){
            sessionExists = true;
        } else {
            sessionExists = false;
        }
        return sessionExists;        
    }
    @Transactional
    public DirectMessageSession getSessionOrCreateSession(User u1, User u2) {
    // 1) look for an existing session in either order
    Optional<DirectMessageSession> opt =
        directMessageSessionRepository.findSessionBetweenUsers(u1.getId(), u2.getId());
    if (opt.isPresent()) {
        return opt.get();
    }

    // 2) not found → attempt to create one (constructor now normalizes order)
    try {
        DirectMessageSession s = new DirectMessageSession(u1, u2);
        return directMessageSessionRepository.save(s);
    } catch (DataIntegrityViolationException e) {
        return directMessageSessionRepository
        .findSessionBetweenUsers(u1.getId(), u2.getId())
        .orElseThrow(() -> new RuntimeException("Failed to create or find session", e));
    }
    } 

    // Send a message with SENT status initially.
    public DirectMessages sendMessage(User sender, Long sessionId, String content){
        DirectMessageSession session = directMessageSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        DirectMessages message = new DirectMessages(session, sender, content, MessageStatus.SENT);
        return directMessageRepository.save(message);
    }

    public List<DirectMessages> getAllMessagesFromSession(DirectMessageSession session){
        return directMessageRepository.findBySessionId(session.getId());
    }

    // Update messages to READ if they are not sent by the receiver.
    public void updateMessageRead(DirectMessageSession session, User receiver, Boolean active) {
        // Only update messages if the read receipt indicates the receiver is active (i.e. viewing the chat)
        if (active == null || !active) {
            return;
        }
        
        List<DirectMessages> messages = directMessageRepository.findBySessionId(session.getId());
        for (DirectMessages message : messages) {
            // Only mark as READ if the message wasn't sent by the receiver and is not already READ
            if (!message.getSender().getId().equals(receiver.getId()) 
                    && message.getMessageStatus() != MessageStatus.READ) {
                message.setMessageStatus(MessageStatus.READ);
                message.setReadAt(LocalDateTime.now());
                directMessageRepository.save(message);
            }
        }
    }    
}
