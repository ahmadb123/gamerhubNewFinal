package com.example.demo.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.controllers.FriendsController.FriendsController;
import com.Repository.UserRepository;
import com.services.FriendsService;
import com.utility.JWT;
import com.models.UserModel.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Repository.FriendsRepository; // add this import

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(FriendsController.class)
public class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private FriendsService friendsService;
    
    @MockBean  
    private FriendsRepository friendsRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testAddFriend_Success() throws Exception {
        String token = "dummyToken";
        when(jwt.extractUsername(token)).thenReturn("requesterUser");
        
        // Prepare mock users for requester and target.
        User requester = new User();
        requester.setId(1L);
        requester.setUsername("requesterUser");
        when(userRepository.findByUsername("requesterUser")).thenReturn(java.util.Optional.of(requester));
        
        User target = new User();
        target.setId(2L);
        target.setUsername("targetUser");
        when(userRepository.findByUsername("targetUser")).thenReturn(java.util.Optional.of(target));
        
        mockMvc.perform(post("/api/friends/add")
                .header("Authorization", "Bearer " + token)
                .param("userNameOFRequest", "targetUser"))
            .andExpect(status().isOk())
            .andExpect(content().string("Friend request sent"));
        
        verify(friendsService, times(1)).sendFriendRequest(any(User.class), any(User.class));
    }
}
