package com.controllers.SaveUserGamesController;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Repository.UserRepository;
import com.models.NewsModel.NewsResults;
import com.models.UserModel.User;
import com.models.UserSavedGames.UserSavedCollection.MyCollection;
import com.services.CollectionService;
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
    @Autowired 
    private CollectionService collectionService; // helps save the game to the user's collection
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
        return ResponseEntity
            .ok(Collections.singletonMap("message","Game saved successfully"));
            } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body(Collections.singletonMap("error","An error occurred"));
        }
    }
    // delete saved game - 
    @DeleteMapping("/delete-game")
    public ResponseEntity<?> deleteSavedGame(@RequestHeader("Authorization") String authHeader, 
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

            // delete the game from the user's profile
            saveGamesService.deleteGame(userId, gameId.getId());
        return ResponseEntity
            .ok(Collections.singletonMap("message","Game deleted successfully"));
        } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity
            .badRequest()
            .body(Collections.singletonMap("error","An error occurred"));
        }
    }
    @PostMapping("/add-to-wishlist")
    public ResponseEntity<?> saveToWishlist(@RequestHeader("Authorization") String authHeader, 
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
        saveGamesService.saveToWishlist(user, gameId);
        return ResponseEntity
            .ok(Collections.singletonMap("message","Game added to wishlist successfully"));
        } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity
            .badRequest()
            .body(Collections.singletonMap("error","An error occurred"));
        }
    }

    @GetMapping("get-collection")
    public ResponseEntity<?> getUserCollections(@RequestHeader("Authorization") String auth){
        var list = collectionService.listUserCollections(auth.substring(7));
        return ResponseEntity.ok(list);
    }

    @GetMapping("get-friends-collection/{id}")
    public ResponseEntity<?> getFriendsCollection(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id
    ){
        var list = collectionService.listFriendsCollections(auth.substring(7), id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add-to-collection")
    public ResponseEntity<?> createCollection(  @RequestHeader("Authorization") String auth,
                                    @RequestBody Map<String,String> body){
        MyCollection c = collectionService.createCollection(auth.substring(7), body.get("name"));
        return ResponseEntity.ok(Collections.singletonMap("collection",c));        
    }


    @PostMapping("/{id}/games")
    public ResponseEntity<?> addGame(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id,
            @RequestBody Map<String,Long> body
    ) {
    collectionService.addGameToCollection(auth.substring(7), id, body.get("gameId"));
    return ResponseEntity.ok(Collections.singletonMap("message","Added"));
  }
  
}
