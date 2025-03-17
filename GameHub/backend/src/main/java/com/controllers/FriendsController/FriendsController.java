package com.controllers.FriendsController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Repository.FriendsRepository;
import com.Repository.UserRepository;
import com.models.FriendsModel.Friends;
import com.models.UserModel.User;
import com.models.XboxModel.XboxProfile;
import com.services.FriendsService;
import com.utility.JWT;
import com.dto.FriendWithXboxDTO;
import com.dto.PendingRequestDTO;

@RestController
@RequestMapping("/api/friends")
public class FriendsController {
    @Autowired
    private JWT jwt;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserRepository userRepository; // Repository for User lookups
    @Autowired
    private FriendsRepository friendsRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addFriend(@RequestHeader("Authorization") String authHeader,
        @RequestParam("userNameOFRequest") String targetUserName) {
        // Extract token (assume "Bearer " prefix)
        String token = authHeader.replace("Bearer ", "");
        String requesterUserName = jwt.extractUsername(token);
        // Find users
        User requester = userRepository.findByUsername(requesterUserName)
            .orElseThrow(() -> new RuntimeException("User not found"));
        User target = userRepository.findByUsername(targetUserName)
            .orElseThrow(() -> new RuntimeException("Target user not found"));
        // Process friend request.
        friendsService.sendFriendRequest(requester, target);
        return ResponseEntity.ok("Friend request sent");
    }
    @PostMapping("/accept")
    public ResponseEntity<?> acceptRequest(@RequestParam String userNameOFRequest, @RequestHeader("Authorization") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            String loggedInUser = jwt.extractUsername(token);
            if(loggedInUser == null){
                System.out.println("Invalid token or username not found");
                return ResponseEntity.status(401).body("Invalid token or username not found");
            }

            // get the user we want to add as a friend - 

            Optional<User> requesterOpt = userRepository.findByUsername(loggedInUser);
            Optional<User> targetUserOpt = userRepository.findByUsername(userNameOFRequest);

            if (!requesterOpt.isPresent()) {
                return ResponseEntity.status(404).body("Requester not found");
            }
            if (!targetUserOpt.isPresent()) {
                return ResponseEntity.status(404).body("Target user not found");
            }

            User requester = requesterOpt.get();
            User targetUser = targetUserOpt.get();

            // look up friend request -
            Optional<Friends> friendRequets = friendsRepository.findByUserIdAndFriendId(targetUser.getId(), requester.getId());
            if(friendRequets.isEmpty()){
                return ResponseEntity.status(404).body("Friend request not found");
            }

            // get the friend request id -
            Long friendRequestId = friendRequets.get().getId();
            // send friend request - 
            friendsService.acceptFriendRequest(friendRequestId);
            return ResponseEntity.ok("Friend request accepted");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String loggedInUsername = jwt.extractUsername(token);
        // find user by username
        Optional<User> userOpt = userRepository.findByUsername(loggedInUsername);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        User user = userOpt.get();
        List<Friends> pendingList = friendsRepository.findPendingRequestsForUser(user.getId());
        List<PendingRequestDTO> dtos = pendingList.stream().map(friend -> {
            PendingRequestDTO dto = new PendingRequestDTO();
            dto.setId(friend.getId());
            dto.setUsername(friend.getUser().getUsername());
            dto.setStatus(friend.getStatus());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // get all friends - 
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllFriends(@RequestHeader("Authorization") String authHeader){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String token = authHeader.substring(7);
            String loggedInUser = jwt.extractUsername(token);
            Long userId = jwt.extractUserId(token);
            if (loggedInUser == null) {
                System.out.println("Invalid token or username not found");
                return ResponseEntity.status(401).body("Invalid token or username not found");
            }

            // 1) Get all friend User objects
            List<User> friends = friendsService.getAllFriends(userId);
            if (friends.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // 2) Transform each friend into a DTO
            List<FriendWithXboxDTO> friendDTOs = friends.stream()
                .map(friend -> {
                    FriendWithXboxDTO dto = new FriendWithXboxDTO();
                    dto.setUserId(friend.getId());
                    dto.setUsername(friend.getUsername());
                    dto.setEmail(friend.getEmail());  
                    
                    // Could fetch the entire list of XboxProfiles
                    dto.setXboxProfiles(friend.getXboxProfiles());
                    
                    // If you only want the first profile or certain fields, handle that here
                    return dto;
                })
                .collect(Collectors.toList());

            // 3) Return the list of DTOs
            return ResponseEntity.ok(friendDTOs);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
