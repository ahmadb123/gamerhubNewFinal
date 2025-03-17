package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.SaveUserGamesController.SaveGamesController;
import com.models.NewsModel.NewsResults;
import com.models.UserModel.User;
import com.Repository.UserRepository;
import com.services.SaveGamesService;
import com.utility.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SaveGamesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SaveGamesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private SaveGamesService saveGamesService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testSaveGame_Success() throws Exception {
        NewsResults dummyGame = new NewsResults();
        dummyGame.setId(100L);
        
        // Simulate extraction of user id from token.
        when(jwt.extractUserId("dummyToken")).thenReturn(1L);
        // Simulate finding the user.
        User dummyUser = new User();
        dummyUser.setId(1L);
        dummyUser.setUsername("testuser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        // Simulate saving the game (do nothing)
        doNothing().when(saveGamesService).saveGame(any(User.class), any(NewsResults.class));
        
        mockMvc.perform(post("/api/save-games/save-game")
                .header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyGame)))
            .andExpect(status().isOk())
            .andExpect(content().string("Game saved successfully"));
    }
}
