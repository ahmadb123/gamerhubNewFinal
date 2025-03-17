package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxFriendsController;
import com.models.XboxModel.XboxPeopleFriends;
import com.services.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = XboxFriendsController.class)
public class XboxFriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;
    
    @Test
    public void testGetTopTenFriends_Success() throws Exception {
        // Stub TokenService to return dummy header and xuid.
        when(tokenService.getXboxAuthorizationHeader()).thenReturn("XBL3.0 x=dummyhash;dummyToken");
        when(tokenService.getXuid()).thenReturn("dummyXuid");
        
        // Simulate RestTemplate exchange returning a JSON string.
        String dummyJson = "{\"friends\": []}";
        when(restTemplate.exchange(any(String.class), any(), any(), any(Class.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok(dummyJson));
        
        // Perform GET request
        mockMvc.perform(get("/api/xbox/friends/top-ten"))
            .andExpect(status().isOk());
            // You can add further JSON path assertions once you know the expected structure.
    }
}
