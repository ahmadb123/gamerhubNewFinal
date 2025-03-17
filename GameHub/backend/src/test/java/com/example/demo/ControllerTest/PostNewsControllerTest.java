package com.example.demo.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.controllers.CommunityInsightController.PostNewsController;
import com.dto.AuthResponseDTO;
import com.models.CommunityInsight.PostNews;
import com.models.UserModel.User;
import com.Repository.UserRepository;
import com.services.PostNewsService;
import com.utility.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostNewsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostNewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private PostNewsService postNewsService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testShareNews_Success() throws Exception {
        String token = "dummyToken";
        // Stub JWT to extract a username
        when(jwt.extractUsername(token)).thenReturn("testuser");
        
        // Stub repository to return a user when looked up
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        
        // Create a sample PostNews object to be sent in the request
        PostNews postNews = new PostNews();
        postNews.setContentText("Test news post");
        String jsonContent = objectMapper.writeValueAsString(postNews);
        
        // Perform the POST request with a Bearer token header and JSON content
        mockMvc.perform(post("/api/community-insight/post-news")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Post created successfully"));
        
        // Verify that the service's createPost method was called once
        verify(postNewsService, times(1)).createPost(any(User.class), any(PostNews.class));
    }
}
