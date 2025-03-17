package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.SearchUserController;
import com.dto.XboxProfileDTO;
import com.dto.XboxRecentGameDTO;
import com.services.XboxProfileService;
import com.services.XboxRecentGamesService;
import com.utility.JWT;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SearchUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SearchUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private XboxProfileService xboxProfileService;
    
    @MockBean
    private XboxRecentGamesService xboxRecentGamesService;
    
    @Test
    public void testSearchUser_Success() throws Exception {
        // Stub JWT extraction
        when(jwt.extractUsername("dummyToken")).thenReturn("testuser");
        // Stub xboxProfileService.getSearchedProfileData to return a dummy profile DTO.
        XboxProfileDTO dummyDto = new XboxProfileDTO();
        dummyDto.setGamertag("TestGamer");
        when(xboxProfileService.getSearchedProfileData("searchUser")).thenReturn(dummyDto);
        // Stub xboxRecentGamesService.getRecentGamesByUsername to return a dummy list.
        XboxRecentGameDTO dummyGame = new XboxRecentGameDTO();
        dummyGame.setGameName("Recent Game");
        when(xboxRecentGamesService.getRecentGamesByUsername("searchUser")).thenReturn(Arrays.asList(dummyGame));
        
        mockMvc.perform(get("/api/search")
                .param("username", "searchUser")
                .header("Authorization", "Bearer dummyToken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.profile.gamertag").value("TestGamer"))
            .andExpect(jsonPath("$.recentPlayedGames[0].gameName").value("Recent Game"));
    }
    
    @Test
    public void testGetAllLinkedProfiles_Success() throws Exception {
        // Stub JWT extraction for user id.
        when(jwt.extractUserId("dummyToken")).thenReturn(1L);
        // Stub xboxProfileService.getAllLinkedAccounts to return a dummy list.
        XboxProfileDTO profile1 = new XboxProfileDTO();
        profile1.setGamertag("GamerOne");
        XboxProfileDTO profile2 = new XboxProfileDTO();
        profile2.setGamertag("GamerTwo");
        when(xboxProfileService.getAllLinkedAccounts(1L)).thenReturn(Arrays.asList(profile1, profile2));
        
        mockMvc.perform(get("/api/search/all-linked-profiles")
                .header("Authorization", "Bearer dummyToken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].gamertag").value("GamerOne"))
            .andExpect(jsonPath("$[1].gamertag").value("GamerTwo"));
    }
}
