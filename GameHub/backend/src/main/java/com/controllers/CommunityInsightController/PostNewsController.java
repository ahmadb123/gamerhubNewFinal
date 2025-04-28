package com.controllers.CommunityInsightController;

import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.models.UserModel.User;
import com.Repository.UserRepository;
import com.Repository.MessagesRepository.DirectMessageRepository;
import com.dto.DirectMessageDTO;
import com.dto.PostNewsDTO;
import com.dto.ReplyNewsDTO;
import com.models.ChatsAndDirectMessages.DirectMessageSession;
import com.models.ChatsAndDirectMessages.DirectMessages;
import com.models.ChatsAndDirectMessages.MessageStatus;
import com.models.CommunityInsight.Interaction;
import com.models.CommunityInsight.PostNews;
import com.services.InteractionService;
import com.services.PostNewsService;
import com.services.MessagesService.DirectMessagesService;
import com.utility.JWT;


@RestController
@RequestMapping("/api/community-insight")
public class PostNewsController {

    @Autowired
    private JWT jwt;

    @Autowired
    private UserRepository userRepository; // find username

    @Autowired 
    private DirectMessagesService directMessagesService;
    @Autowired
    private PostNewsService postNewsService;
    @Autowired 
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private DirectMessageRepository directMessageRepository;
    @Autowired
    InteractionService interactionService;

    // post news could be posting news/sharing news to community
    @PostMapping("/post-news")
    public ResponseEntity<?> shareNews(@RequestHeader("Authorization") String authHeader, @RequestBody PostNews postNews){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwt.extractUsername(token); // get username from token
            if(username == null){
                return ResponseEntity.status(401).body("Invalid token or username not found");
            }
            // user details - 
            // get user details from database -
            User user = userRepository.findByUsername(username).orElse(null);
            if(user == null){
                return ResponseEntity.status(401).body("User not found");
            }
            System.out.println("User: "+ user.getUsername());
            postNews.setUser(user); // set user in postNews object
            // create post
            postNewsService.createPost(user, postNews);
            return ResponseEntity.ok(Map.of("message", "Post created successfully")); // Return JSON response
        }catch(Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/news/all")
    public ResponseEntity<List<PostNewsDTO>> getAllNews() {
        // 1) fetch the raw DTOs (enriched with game info, etc.)
        List<PostNewsDTO> dtos = postNewsService.getSharedNewsDetails();
    
        // 2) fill in like/comment counts on each one
        for (PostNewsDTO dto : dtos) {
            dto.setLikesCount(   interactionService.countLikes(dto.getId()) );
            dto.setCommentsCount(interactionService.countComments(dto.getId()) );
        }
    
        // 3) return the modified list
        return ResponseEntity.ok(dtos);
    }
    
    

    // reply to news publisher - 
    @PostMapping("/news/reply")
    public ResponseEntity<?> replyToPublisher(
        @RequestHeader("Authorization") String authHeader, 
        @RequestBody ReplyNewsDTO replyNewsDTO
    ) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            Long senderId = jwt.extractUserId(token);
            if (senderId == null) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            User sender = userRepository.findById(senderId).orElse(null);
            if (sender == null) {
                return ResponseEntity.status(401).body("User not found");
            }
    
            // 1) Retrieve the post & publisher
            PostNews postNews = postNewsService.getSharedNewsDetailsById(replyNewsDTO.getNewsId());
            if (postNews == null) {
                return ResponseEntity.badRequest().body("Post not found");
            }
            User publisher = postNews.getUser();
            if (publisher == null) {
                return ResponseEntity.badRequest().body("Publisher not found");
            }
    
            // 2) Get or create a direct message session between sender & publisher
            DirectMessageSession session = directMessagesService.getSessionOrCreateSession(sender, publisher);
    
            Long numericPostId = postNews.getId(); // get the id num of the post from DB
            String userReply = replyNewsDTO.getReply();          // typed text
            String referenceUrl;
            String referenceType;
            // check if the post num is a clip or image 
            if(postNews.getSharedClipsUrl() != null){
                // if shared clip url is not null, then it is a clip
                referenceUrl = postNews.getSharedClipsUrl();
                referenceType = "clip";
            }else{
                // its an image 
                referenceUrl = replyNewsDTO.getbackgroundImage();
                referenceType = "image";
            }
            String replyContent = String.format(
                "Reply to %s Post #%d: %s%n[%s Reference] %s",
                referenceType, numericPostId, userReply, referenceType, referenceUrl
            );
            // Send the message
            DirectMessages newMessage = directMessagesService.sendMessage(
                sender, 
                session.getId(), 
                replyContent
            );
    
            // Mark it as DELIVERED, store, broadcast, etc.
            newMessage.setMessageStatus(MessageStatus.DELIVERED);
            newMessage.setDeliveredAt(LocalDateTime.now());
            directMessageRepository.save(newMessage);
    
            DirectMessageDTO dto = new DirectMessageDTO(
                newMessage.getId(),
                newMessage.getSender().getUsername(),
                newMessage.getContent(),
                newMessage.getMessageStatus(),
                newMessage.getDeliveredAt(),
                newMessage.getReadAt()
            );
            messagingTemplate.convertAndSend("/topic/direct-message/" + session.getId(), dto);
    
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Reply sent to direct messages.",
                "sessionId", session.getId()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/news/{postId}/like")
    public ResponseEntity<?> like(@RequestHeader("Authorization") String auth,
                                    @PathVariable Long postId){
        try{
            Long userId = jwt.extractUserId(auth.substring(7));
            if(userId == null){
                return ResponseEntity.status(401).body("Invalid token");
            }
            long totalLikes = interactionService.toggleLike(postId, userId);
            return ResponseEntity.ok(Map.of("totalLikes", totalLikes));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/news/{postId}/comment")
    public ResponseEntity<?> comments(@RequestHeader("Authorization") String auth,
                                        @PathVariable Long postId,
                                        @RequestBody Map<String,String> body){
        try{
            Long userId = jwt.extractUserId(auth.substring(7));
            Interaction comment = interactionService.addComment(postId, userId, body.get("comment"));
            return ResponseEntity.ok(Map.of(
                    "id", comment.getId(),
                    "user", comment.getUser().getUsername(),
                    "text", comment.getCommentText(),
                    "time", comment.getTimestamp()
            ));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }                               
    }  
    @GetMapping("/news/{postId}/comments")
    public ResponseEntity<?> comments(@PathVariable Long postId) {
      List<Interaction> comments = interactionService.getComments(postId);
      return ResponseEntity.ok(comments.stream().map(c->Map.of(
        "user", c.getUser().getUsername(),
        "text", c.getCommentText(),
        "time", c.getTimestamp()
      )).toList());
    }
}  
    