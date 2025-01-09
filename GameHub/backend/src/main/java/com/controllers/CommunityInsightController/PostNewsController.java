package com.controllers.CommunityInsightController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.models.UserModel.User;
import com.Repository.UserRepository;
import com.models.CommunityInsight.PostNews;
import com.services.PostNewsService;
import com.services.UserDetailsService.UserService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/community-insight")
public class PostNewsController {

    @Autowired
    private JWT jwt;

    @Autowired
    private UserRepository userRepository; // find username


    @Autowired
    private PostNewsService postNewsService;
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
            
            // create post
            postNewsService.createPost(user, postNews);
            return ResponseEntity.ok(Map.of("message", "Post created successfully")); // Return JSON response
        }catch(Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

}
