package com.controllers.GameTrailerFromYoutubeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.YoutubeDataForGameVideos.YoutubeVideosModel;
import com.services.GameTrailerFromYoutubeService;

@RestController
@RequestMapping("/api/game-trailers")
public class GameTrailersController {
    @Autowired
    private GameTrailerFromYoutubeService gameTrailersService;

    @GetMapping("/game-trailers/{gameName}")
    public ResponseEntity<?> getGameTrailers(@PathVariable String gameName) {
        try{
         YoutubeVideosModel gameTrailers = gameTrailersService.getGameTrailerFromYoutube(gameName);
            return ResponseEntity.ok(gameTrailers);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }
}