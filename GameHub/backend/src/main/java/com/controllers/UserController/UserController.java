package com.controllers.UserController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.dto.UpdatedPasswordDTO;
import com.dto.UserDTO;
import com.dto.UsernameUpdateDTO;
import com.services.UserDetailsService.UserService;
import com.utility.JWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/update-bio")
    public ResponseEntity<?> setOrPostBio(@RequestHeader ("Authorization") String authHeader, @RequestBody String bio) {
       try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token);
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }

            if(bio == null || bio.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Bio cannot be empty"));
            }
            String bioResponse =  userService.updateUserBio(userId, bio);
            return ResponseEntity.ok(bioResponse);
       }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
    }

   @GetMapping("/get-bio")
   public ResponseEntity<?> getBio(@RequestHeader ("Authorization") String authHeader) {
       try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token);
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }

            String bioResponse =  userService.getUserBio(userId);
            return ResponseEntity.ok(bioResponse);
       }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
   }

   @PostMapping("/update-username")
   public ResponseEntity<?> updateUsername(@RequestHeader ("Authorization") String authHeader, @RequestBody UsernameUpdateDTO updatedUsername) {
       try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token);
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }

            if(updatedUsername == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Username cannot be empty"));
            }
            String newUsername = updatedUsername.getNewUsername();
            String usernameResponse =  userService.updateUsername(userId, newUsername);
            // generate new token = 
            String newToken = jwt.generateToken(userId, usernameResponse);
            return ResponseEntity.ok(Map.of(
                "message", "Username updated successfully",
                "newUsername", usernameResponse,
                "newToken", newToken // Include the new token
            ));
           }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
   }
   @GetMapping("/get-username")
   public ResponseEntity<?> getUsername(@RequestHeader ("Authorization") String authHeader) {
       try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            String username = jwt.extractUsername(token);
            if(username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            UserDTO user = new UserDTO(username);
            return ResponseEntity.ok(user);
       }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
   }

   @GetMapping("/get-password")
   public ResponseEntity<?> getPassword(@RequestHeader ("Authorization") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            String username = jwt.extractUsername(token);
            if(username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            // find user - 
            UserDetails user = userService.loadUserByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            String password = user.getPassword();
            return ResponseEntity.ok(Map.of("password", password));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
   }

   @PostMapping("/update-password")
   public ResponseEntity<?> updatePassword(@RequestHeader ("Authorization") String authHeader , @RequestBody UpdatedPasswordDTO updatedPasswordDTO){
        // update password
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            String token = authHeader.substring(7);
            String username  = jwt.extractUsername(token);
            Long userId = jwt.extractUserId(token);
            if(username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Unauthorized"));
            }
            UserDetails getUserDetails = userService.loadUserByUsername(username);
            if (getUserDetails == null) {
                throw new RuntimeException("User not found");
            }

            // check if password (current) matches the stored hash - 
            if(!passwordEncoder.matches(updatedPasswordDTO.getCurrentPassword(),getUserDetails.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Current password is incorrect"));
            }
            String passwordResponse = userService.updatePassword(userId, updatedPasswordDTO.getNewPassword());
            return ResponseEntity.ok(Map.of(
                "message", "Password updated successfully",
                "newPassword", passwordResponse
            ));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
   }
}

