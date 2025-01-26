package com.controllers.XboxController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.Repository.XboxProfileRepository;
import com.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.UserModel.User;
import com.models.XboxModel.RecentGamesXbox;
import com.models.XboxModel.XboxProfile;
import com.services.TokenService;
import com.services.XboxRecentGamesService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/xbox/recent-games")
public class XboxRecentPlayedGamescontroller {
    private static final String RECENT_GAMES_URL = "https://titlehub.xboxlive.com/users/xuid({xuid})/titles/titlehistory/decoration/detail?maxItems=5";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private XboxProfileRepository profile;
    @Autowired 
    private XboxRecentGamesService xboxRexGamesService;
    @Autowired
    private JWT jwt;
    /*
     * requires headers: 
     * Authorization: XBL3.0 x={userhash};{token}
     * x-xbl-contract-version: 2
     * Accept-Language: en-US
     * User XUID
     */
    @GetMapping
    public ResponseEntity<?> getRecentGames(@RequestHeader("Auth") String authHeader){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }
            // find user - 
            String token = authHeader.substring(7);
            String loggedInUser = jwt.extractUsername(token);
            if(loggedInUser == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or username not found");
            }
            String apiAuthHeader = tokenService.getXboxAuthorizationHeader();
            String xuid = tokenService.getXuid();
            if(xuid == null){
                System.out.println("XUID not found");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("XUID not found");
            }
            System.out.println("RECENT GAMES: " + xuid);
            // send the request 
            String url = RECENT_GAMES_URL.replace("{xuid}", xuid);
            System.out.println("Request URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", apiAuthHeader);
            headers.set("x-xbl-contract-version", "2"); 
            headers.set("Accept-Language", "en-US"); // Add Accept-Language header
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            // parse json response -
            ObjectMapper mapper = new ObjectMapper();
            RecentGamesXbox recentGames = mapper.readValue(response.getBody(), RecentGamesXbox.class);

            // GET USERNAME 
            User user = userRepository.findByUsername(loggedInUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
            XboxProfile xboxProfile = profile.findByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Xbox profile not found"));
            // call xboxrecentgame service to save recent games
            xboxRexGamesService.saveRecentGames(recentGames, xboxProfile);
            xboxRexGamesService.getRecentGames(loggedInUser);
            // return response = 
            return ResponseEntity.ok(recentGames);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch recent games");
        }  
    }
}
