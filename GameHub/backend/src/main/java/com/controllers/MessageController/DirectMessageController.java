package com.controllers.MessageController;

import java.time.LocalDateTime;
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

import com.Repository.MessagesRepository.DirectMessageRepository;
import com.Repository.MessagesRepository.DirectMessageSessionRepository;
import com.Repository.UserRepository;
import com.dto.DirectMessageDTO;
import com.dto.DirectMessageSessionDTO;
import com.dto.ReadReceiptDTO;
import com.models.ChatsAndDirectMessages.DirectMessageSession;
import com.models.ChatsAndDirectMessages.DirectMessages;
import com.models.ChatsAndDirectMessages.MessageStatus;
import com.models.UserModel.User;
import com.services.MessagesService.DirectMessagesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/dm")
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
    
    @Autowired
    private DirectMessageRepository directMessageRepository;

    // WebSocket endpoint to handle sending direct messages in real time.
    @MessageMapping("/direct-message/{sessionId}/send")
    public void handleDirectMessage(@DestinationVariable Long sessionId,
                                    DirectMessages message,
                                    @Header("Authorization") String header) {
        // Validate and extract sender
        if (header == null) {
            throw new RuntimeException("Authorization header missing");
        }
        String jwtToken = header.substring(7);
        User sender = userRepository.findById(jwt.extractUserId(jwtToken))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save message with SENT status
        DirectMessages savedMessage = directMessagesService.sendMessage(sender, sessionId, message.getContent());

        // Set the message status to DELIVERED
        savedMessage.setMessageStatus(MessageStatus.DELIVERED);
        savedMessage.setDeliveredAt(LocalDateTime.now());
        directMessageRepository.save(savedMessage);

        // Build and send the message DTO (with DELIVERED status)
        DirectMessageDTO dto = new DirectMessageDTO(
            savedMessage.getId(),
            savedMessage.getSender().getUsername(),
            savedMessage.getContent(),
            savedMessage.getMessageStatus(),
            savedMessage.getDeliveredAt(),
            savedMessage.getReadAt()
        );
        messagingTemplate.convertAndSend("/topic/direct-message/" + sessionId, dto);
    }

    // Check if a session exists between the current user and userTwo
    @GetMapping("/api/getSession/{userTwoId}")
    public ResponseEntity<?> getSession(@RequestHeader("Authorization") String header,
                                        @PathVariable Long userTwoId) {
        if (header == null) {
            return ResponseEntity.badRequest().body("Authorization header is missing");
        }
        String token = header.substring(7);
        Long loggedInUserId = jwt.extractUserId(token); 
        if (loggedInUserId == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User getUserOne = userRepository.findById(loggedInUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        User userTwo = userRepository.findById(userTwoId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        boolean sessionExists = directMessagesService.checkIfSessionExists(getUserOne, userTwo);
        return ResponseEntity.ok(sessionExists ? "Session exists" : "Session does not exist");
    }

    // Create or get a session
    @PostMapping("/api/creatOrGetSession/{userTwoId}")
    public ResponseEntity<?> createOrGetSession(@RequestHeader("Authorization") String header,
                                                @PathVariable Long userTwoId) {
        if (header == null) {
            return ResponseEntity.badRequest().body("Authorization header is missing");
        }
        String token = header.substring(7);
        Long loggedInUserId = jwt.extractUserId(token);
        if (loggedInUserId == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        User getUserOne = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User userTwo = userRepository.findById(userTwoId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create or get session
        DirectMessageSession session = directMessagesService.getSessionOrCreateSession(getUserOne, userTwo);
        DirectMessageSessionDTO sessionDTO = DirectMessageSessionDTO.fromEntity(session);
        return ResponseEntity.ok(sessionDTO);
    }

    // Get all messages from a session
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
                .map(msg -> new DirectMessageDTO(
                        msg.getId(),
                        msg.getSender().getUsername(),
                        msg.getContent(),
                        msg.getMessageStatus(),
                        msg.getDeliveredAt(),
                        msg.getReadAt()
                ))
                .collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Session not found");
        }
    }

    @MessageMapping("/direct-message/{sessionId}/read")
    public void handleReadReceipt(@DestinationVariable Long sessionId,
                                  ReadReceiptDTO receipt,
                                  @Header("Authorization") String header) {
        if(header == null) {
            throw new RuntimeException("Authorization header missing");
        }
        String jwtToken = header.substring(7);
        User receiver = userRepository.findById(jwt.extractUserId(jwtToken))
                .orElseThrow(() -> new RuntimeException("User not found"));
        DirectMessageSession session = directMessageSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    
        // Pass the 'active' flag from the receipt
        directMessagesService.updateMessageRead(session, receiver, receipt.getActive());
    
        receipt.setEventType("READ_RECEIPT");
        receipt.setMsgStatus(MessageStatus.READ);
    
        messagingTemplate.convertAndSend("/topic/direct-message/" + sessionId, receipt);
    }
    
}
