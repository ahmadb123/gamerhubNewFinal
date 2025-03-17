package com.example.demo.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.controllers.LoginController.Authentication;
import com.dto.AuthResponseDTO;
import com.dto.UserDTO;
import com.services.AuthService;
import com.utility.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(Authentication.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthService authService;
    
    @MockBean
    private JWT jwt;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testLogin_Success() throws Exception {
        // Prepare the user DTO
        UserDTO userDto = new UserDTO();
        userDto.setUsename("testuser");
        userDto.setPassword("password123");
        
        // Prepare dummy token and corresponding user details
        String dummyToken = "dummyToken";
        AuthResponseDTO authResponse = new AuthResponseDTO(dummyToken, 1L, "testuser");
        when(authService.authenticate("testuser", "password123")).thenReturn(dummyToken);
        when(jwt.extractUserId(dummyToken)).thenReturn(1L);
        when(jwt.extractUsername(dummyToken)).thenReturn("testuser");
        
        String json = objectMapper.writeValueAsString(userDto);
        
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(dummyToken))
            .andExpect(jsonPath("$.id").value(1)) // changed from $.userId to $.id
            .andExpect(jsonPath("$.username").value("testuser"));
    }
}
