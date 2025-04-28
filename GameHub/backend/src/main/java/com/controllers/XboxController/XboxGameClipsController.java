package com.controllers.XboxController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.models.GameClipRecordXbox.GameClip;
import com.services.GameClipsServiceXbox;
import com.services.TokenService;

@RestController
@RequestMapping("/api/xbox")
public class XboxGameClipsController {
    private static final String GAME_CLIPS_API =
        "https://gameclipsmetadata.xboxlive.com/users/xuid({xuid})/clips";

    @Autowired private TokenService      tokenService;
    @Autowired private RestTemplate      restTemplate;
    @Autowired private GameClipsServiceXbox gameClipsService;

    @GetMapping("/gameclips")
    public ResponseEntity<?> getGameClips() {
        try {
            String auth = tokenService.getXboxAuthorizationHeader();
            String xuid = tokenService.getXuid();
            if (xuid == null || auth == null) {
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("XUID or Authorization header not set");
            }

            String url = GAME_CLIPS_API.replace("{xuid}", xuid);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",           auth);
            headers.set("x-xbl-contract-version", "2");

            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<String> resp = restTemplate.exchange(
                url, HttpMethod.GET, req, String.class
            );

            List<GameClip> clips = gameClipsService.getGameClips(resp.getBody());
            return ResponseEntity.ok(clips);

        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching game clips: " + e.getMessage());
        }
    }

    @GetMapping("/gameclips/{clipId}")
    public ResponseEntity<?> getGameClipById(@PathVariable String clipId) {
        try {
            String auth = tokenService.getXboxAuthorizationHeader();
            String xuid = tokenService.getXuid();
            if (xuid == null || auth == null) {
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("XUID or Authorization header not set");
            }

            // 1) Fetch full list
            String url = GAME_CLIPS_API.replace("{xuid}", xuid);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",           auth);
            headers.set("x-xbl-contract-version", "2");

            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<String> resp = restTemplate.exchange(
                url, HttpMethod.GET, req, String.class
            );

            List<GameClip> clips = gameClipsService.getGameClips(resp.getBody());

            // 2) Filter by ID
            Optional<GameClip> found = clips.stream()
                .filter(c -> clipId.equals(c.getGameClipId()))
                .findFirst();

            return found
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Game clip not found: " + clipId)
                );

        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching game clip: " + e.getMessage());
        }
    }
}
