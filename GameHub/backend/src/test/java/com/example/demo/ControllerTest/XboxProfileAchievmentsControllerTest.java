package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxProfileAchievmentsController;
import com.models.XboxProfileAchievements.Achievements;
import com.services.AchievementsService;
import com.services.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import org.springframework.http.HttpStatus;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = XboxProfileAchievmentsController.class)
public class XboxProfileAchievmentsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private AchievementsService achievementsService;
    
    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;
    
    @Test
    public void testGetAchievements_Success() throws Exception {
        when(tokenService.getXboxAuthorizationHeader()).thenReturn("XBL3.0 x=dummyhash;dummyToken");
        when(tokenService.getXuid()).thenReturn("dummyXuid");
        
        String dummyJson = "{\"achievements\": []}";
        when(restTemplate.exchange(any(String.class), any(), any(), any(Class.class)))
            .thenReturn(ResponseEntity.ok(dummyJson));
        
        when(achievementsService.getAchievements("dummyXuid", dummyJson))
            .thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/xbox/profile/achievements"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}
