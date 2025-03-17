package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.GameTrailerFromYoutubeController.GameTrailersController;
import com.models.YoutubeDataForGameVideos.YoutubeVideosModel;
import com.services.GameTrailerFromYoutubeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = GameTrailersController.class)
public class GameTrailersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GameTrailerFromYoutubeService gameTrailersService;
    
    @Test
    public void testGetGameTrailers_Success() throws Exception {
        // Create a dummy YoutubeVideosModel (adjust as per your model definition)
        YoutubeVideosModel dummyModel = new YoutubeVideosModel();
        dummyModel.setItems(java.util.Collections.emptyList());
        
        when(gameTrailersService.getGameTrailerFromYoutube("TestGame")).thenReturn(dummyModel);
        
        mockMvc.perform(get("/api/game-trailers/game-trailers/TestGame"))
            .andExpect(status().isOk())
            // Expect the returned JSON to represent the empty items list; adjust JSON path as needed.
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items").isEmpty());
    }
}
