package com.services.MessagesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.MessagesRepository.DirectMessageRepository;
import com.Repository.MessagesRepository.DirectMessageSessionRepository;
import com.models.ChatsAndDirectMessages.DirectMessageSession;
import com.models.ChatsAndDirectMessages.DirectMessages;
import com.models.UserModel.User;

@Service
public class DirectMessagesService {
    @Autowired
    private DirectMessageRepository directMessageRepository;
    @Autowired
    private DirectMessageSessionRepository directMessageSessionRepository;
    private boolean sessionExists = false;

    /*
     * check if session exists, if exists then get the messages
     * else create a new session and return empty list
     */
    // function to check if session exists or not-> if yes return true
    // else return false 
    public boolean checkIfSessionExists(User userOneId, User userTwoId){
        // use the repository to check if the session exists
        if(directMessageSessionRepository.findSessionBetweenUsers(userOneId.getId(), userTwoId.getId()).isPresent()){
            sessionExists = true;
        }else{
            sessionExists = false;
        }
        return sessionExists;        
    }

    // if session exist
    public DirectMessageSession getSessionOrCreateSession(User userOne, User userTwo) {
        // Check if a session exists between the two users
        if (checkIfSessionExists(userOne, userTwo)) {
            return directMessageSessionRepository
                    .findSessionBetweenUsers(userOne.getId(), userTwo.getId())
                    .get();
        } else {
            // Create and save a new session if one does not exist
            DirectMessageSession newSession = new DirectMessageSession(userOne, userTwo);
            return directMessageSessionRepository.save(newSession);
        }
    }    

    // send message->
    public DirectMessages sendMessage(User sender, Long sessionId, String content){
        DirectMessageSession session = directMessageSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        DirectMessages message = new DirectMessages(session, sender, content);
        return directMessageRepository.save(message);
    }

    // get all messages from session->
    public List<DirectMessages> getAllMessagesFromSession(DirectMessageSession session){
        return directMessageRepository.findBySessionId(session.getId());
    }
}
