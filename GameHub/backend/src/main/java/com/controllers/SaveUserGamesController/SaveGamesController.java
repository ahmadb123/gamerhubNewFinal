package com.controllers.SaveUserGamesController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Repository.UserRepository;
import com.models.NewsModel.NewsResults;
import com.models.UserModel.User;
import com.services.SaveGamesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/save-games")
public class SaveGamesController {
    @Autowired
    private JWT jwt; // helps locate the user from the token

    @Autowired
    private UserRepository userRepository; // helps locate the user

    @Autowired
    private SaveGamesService saveGamesService; // helps save the game to the user's profile

    @PostMapping("/save-game")
    public ResponseEntity<?> saveGame(@RequestHeader("Authorization") String authHeader, 
                                        @RequestBody NewsResults gameId) {
        try{
            // get the user from the token
            String token = authHeader.substring(7);
            Long userId = jwt.extractUserId(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("Invalid token");
            }
            // get the user
            User user = userRepository.findById(userId).orElse(null);
            if(user == null){
                return ResponseEntity.badRequest().body("User not found");
            }
            System.out.println("User found: " + user.getUsername());

            // save the game to the user's profile
            saveGamesService.saveGame(user, gameId);
            return ResponseEntity.ok("Game saved successfully");
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }
}
