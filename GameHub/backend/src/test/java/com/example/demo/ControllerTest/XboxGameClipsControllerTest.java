package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxGameClipsController;
import com.models.GameClipRecordXbox.GameClips;
import com.services.GameClipsServiceXbox;
import com.services.TokenService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = XboxGameClipsController.class)
public class XboxGameClipsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;
    
    @MockBean
    private GameClipsServiceXbox gameClipsService;
    
    @Test
    public void testGetGameClips_Success() throws Exception {
        when(tokenService.getXboxAuthorizationHeader()).thenReturn("XBL3.0 x=dummyhash;dummyToken");
        when(tokenService.getXuid()).thenReturn("dummyXuid");
        
        String dummyJson = "{\"clips\": []}";
        when(restTemplate.exchange(any(String.class), any(), any(), any(Class.class)))
            .thenReturn(ResponseEntity.ok(dummyJson));
        
        when(gameClipsService.getGameClips(dummyJson)).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/xbox/gameclips"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}
