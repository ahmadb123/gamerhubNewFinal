package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxRecentPlayedGamescontroller;
import com.models.XboxModel.RecentGamesXbox;
import com.models.XboxModel.XboxProfile;
import com.services.TokenService;
import com.services.XboxRecentGamesService;
import com.Repository.UserRepository;
import com.Repository.XboxProfileRepository;
import com.utility.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = XboxRecentPlayedGamescontroller.class)
public class XboxRecentPlayedGamescontrollerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private com.Repository.XboxProfileRepository xboxProfileRepository;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private XboxRecentGamesService xboxRexGamesService;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;
    
    @Test
    public void testGetRecentGames_Success() throws Exception {
        // Stub header extraction from token
        when(jwt.extractUsername("dummyToken")).thenReturn("testuser");
        
        // Stub TokenService methods
        when(tokenService.getXboxAuthorizationHeader()).thenReturn("XBL3.0 x=dummyhash;dummyToken");
        when(tokenService.getXuid()).thenReturn("dummyXuid");
        
        // Stub user repository to return dummy user
        com.models.UserModel.User dummyUser = new com.models.UserModel.User();
        dummyUser.setId(1L);
        dummyUser.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(dummyUser));
        
        // Stub XboxProfileRepository methods to return a dummy XboxProfile
        XboxProfile dummyProfile = new XboxProfile();
        dummyProfile.setXboxGamertag("TestGamer");
        when(xboxProfileRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(dummyProfile));
        when(xboxProfileRepository.findByUserIdAndGamertag(1L, "TestGamer"))
            .thenReturn(Optional.of(dummyProfile));
        
        // Stub RestTemplate exchange to return a dummy JSON
        String dummyJson = "{\"titles\": []}";
        when(restTemplate.exchange(any(String.class), any(), any(), any(Class.class)))
            .thenReturn(ResponseEntity.ok(dummyJson));
        
        // Stub XboxRecentGamesService (for saveRecentGames and getRecentGames) to do nothing / return dummy data
        when(xboxRexGamesService.getRecentGames("testuser")).thenReturn(Collections.emptyList());
        // We assume saveRecentGames runs without error
        
        mockMvc.perform(get("/api/xbox/recent-games")
                .header("Auth", "Bearer dummyToken"))
            .andExpect(status().isOk());
            // You can add further assertions on the JSON content if needed.
    }
}
