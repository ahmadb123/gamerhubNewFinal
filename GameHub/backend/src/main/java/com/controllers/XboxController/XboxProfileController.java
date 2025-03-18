package com.controllers.XboxController;
import com.services.TokenService;
import com.services.UserLinkedProfilesService;
import com.services.XboxProfileService;
import com.utility.JWT;
import com.dto.XboxProfileDTO;
import com.models.UserModel.User;
import com.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/xbox")
public class XboxProfileController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private XboxProfileService xboxProfileService;

    @Autowired
    private JWT jwt;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLinkedProfilesService userLinkedProfilesService;

    private final String PROFILE_API_URL = "https://profile.xboxlive.com/users/me/profile/settings?settings=Gamertag,GameDisplayName,AppDisplayPicRaw,GameDisplayPicRaw,AccountTier,TenureLevel,Gamerscore";


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }   

            String token = authHeader.substring(7);
            String username = jwt.extractUsername(token);
            Long userId = jwt.extractUserId(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or username not found");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            System.out.println("USERRRR: "+ username);
            // Get Authorization Header
            String authorizationHeader = tokenService.getXboxAuthorizationHeader();
            System.out.println("Authorization Header Used: " + authorizationHeader);

            // Prepare HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            headers.set("x-xbl-contract-version", "3"); // Required by the Xbox API

            // Send GET Request
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(PROFILE_API_URL, HttpMethod.GET, requestEntity, String.class);

            // parse json api -> DTO 

            XboxProfileDTO profileDTO = xboxProfileService.parseProfileJson(response.getBody());

            // store XUID- 
            tokenService.setXuid(profileDTO.getId());
            
            // to link user and xboxprofile get username from Authentication controller-  
            // save to db- 
            xboxProfileService.saveProfile(profileDTO, user);
            // also save the profile platform, and gamertag to the linkedp profile table
            if(userId != null){
                User foundUserById = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                userLinkedProfilesService.addXboxProfileToLinkedProfiles(foundUserById);
            }
            return ResponseEntity.ok(profileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch profile");
        }
    }
}
