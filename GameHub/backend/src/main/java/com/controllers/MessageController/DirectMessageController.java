package com.controllers.MessageController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Repository.UserRepository;
import com.Repository.MessagesRepository.DirectMessageSessionRepository;
import com.dto.DirectMessageDTO;
import com.dto.DirectMessageSessionDTO;
import com.models.ChatsAndDirectMessages.DirectMessageSession;
import com.models.ChatsAndDirectMessages.DirectMessages;
import com.models.UserModel.User;
import com.services.MessagesService.DirectMessagesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/dm")
  // This class handles direct messages between users in a chat application.
  // It uses WebSocket for real-time messaging and REST for fetching message history.
  // The class is annotated with @RestController, indicating that it is a RESTful controller.
  // The @RequestMapping annotation specifies the base URL for all endpoints in this controller.
  // The class is also annotated with @Autowired to inject the necessary services.
  // The class uses @MessageMapping to handle WebSocket messages and @GetMapping to handle RESTful requests.
  // The class uses SimpMessagingTemplate to send messages to WebSocket clients.
  // The class uses Direct
public class DirectMessageController {
    @Autowired
    private DirectMessagesService directMessagesService;

    @Autowired 
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private JWT jwt;

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private DirectMessageSessionRepository directMessageSessionRepository;
    // WebSocket endpoint to handle sending direct messages in real time.
    @MessageMapping("/direct-message/{sessionId}/send")
    public void handleDirectMessage(@DestinationVariable Long sessionId, DirectMessages message, @Header("Authorization") String header) {
        // save the message=>
        if(header == null){
            throw new RuntimeException("Authorization header is missing");
        }
        String jwtToken = header.substring(7);
        Long senderId = jwt.extractUserId(jwtToken);
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found")); 
        // the session id could be retrieved from both user 1 and 2?
        DirectMessages savedMessage = directMessagesService.sendMessage(sender, sessionId, message.getContent());
        DirectMessageDTO dto = new DirectMessageDTO(savedMessage.getSender().getUsername(), savedMessage.getContent());
        messagingTemplate.convertAndSend("/topic/direct-message/" + sessionId, dto);
    }

    //  controller for if the user is starting a new session or not->
    @GetMapping("/api/getSession/{userTwoId}")
    public ResponseEntity<?> getSession(@RequestHeader("Authorization") String header, @PathVariable Long userTwoId){
        // check if session exists
        // to get user id 1->
        // we get it from JWT token of the current logged in user-=> and request authorization header 
        if(header == null){
            return ResponseEntity.badRequest().body("Authorization header is missing");
        }
        String token = header.substring(7);
        Long loggedInUserId = jwt.extractUserId(token); // the id of the user "wants to start a convo"
        if(loggedInUserId == null){
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User getUserOne = userRepository.findById(loggedInUserId).orElseThrow(() -> new RuntimeException("User not found"));
        // for user id two. we get that from the friends list of the user-> 
        // each user has friendslist. we can get the id from the frontend using @PathVariable or @RequestParam long user id two
        User userTwo = userRepository.findById(userTwoId).orElseThrow(() -> new RuntimeException("User not found"));
        if(directMessagesService.checkIfSessionExists(getUserOne, userTwo)){
            return ResponseEntity.ok("Session exists");
        }else{
            return ResponseEntity.ok("Session does not exist");
        }
    }
    // controlle to start or get the session->
    @PostMapping("/api/creatOrGetSession/{userTwoId}")
    public ResponseEntity<?> createOrGetSession(@RequestHeader("Authorization") String header, @PathVariable Long userTwoId){
        // check if session exists
        // to get user id 1->
        // we get it from JWT token of the current logged in user-=> and request authorization header 
        if(header == null){
            return ResponseEntity.badRequest().body("Authorization header is missing");
        }
        String token = header.substring(7);
        Long loggedInUserId = jwt.extractUserId(token); // the id of the user "wants to start a convo"
        if(loggedInUserId == null){
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User getUserOne = userRepository.findById(loggedInUserId).orElseThrow(() -> new RuntimeException("User not found"));
        // for user id two. we get that from the friends list of the user-> 
        // each user has friendslist. we can get the id from the frontend using @PathVariable or @RequestParam long user id two
        User userTwo = userRepository.findById(userTwoId).orElseThrow(() -> new RuntimeException("User not found"));
        // create or get session
        DirectMessageSession session = directMessagesService.getSessionOrCreateSession(getUserOne, userTwo);
        // dto to return
        DirectMessageSessionDTO sessionDTO = DirectMessageSessionDTO.fromEntity(session);
        return ResponseEntity.ok(sessionDTO);
    }

    @GetMapping("/api/get-message-session/{sessionId}")
    public ResponseEntity<?> getAllSessionMessages(@PathVariable Long sessionId) {
        try {
            if(sessionId == null){
                return ResponseEntity.badRequest().body("Session id is missing");
            }
            DirectMessageSession session = directMessageSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            List<DirectMessages> messages = directMessagesService.getAllMessagesFromSession(session);
            List<DirectMessageDTO> dtoList = messages.stream()
                    .map(msg -> new DirectMessageDTO(msg.getSender().getUsername(), msg.getContent()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Session not found");
        }
    }

}
