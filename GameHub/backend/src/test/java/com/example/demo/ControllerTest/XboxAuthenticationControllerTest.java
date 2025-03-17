package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxAuthenticationController;
import com.models.XboxModel.LoginResponse;
import com.models.XboxModel.MobileLoginResponse;
import com.services.OAuth2Service;
import com.services.TokenService;
import com.utility.PKCEUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = XboxAuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class XboxAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OAuth2Service oAuth2Service;
    
    @MockBean
    private TokenService tokenService;
    
    @Test
    public void testLogin_ReturnsLoginResponse() throws Exception {
        // Stub the OAuth2Service to return a dummy URL for login.
        String dummyUrl = "https://login.example.com/auth?code_challenge=abc";
        when(oAuth2Service.generateAuthUrl(anyString())).thenReturn(dummyUrl);
        
        // Perform GET /api/auth/login; we cannot easily test session attributes without further setup,
        // but we can assert that the response JSON contains the URL.
        mockMvc.perform(get("/api/auth/login"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.url").value(dummyUrl));
    }
    
    @Test
    public void testMobileLogin_ReturnsMobileLoginResponse() throws Exception {
        String dummyUrl = "https://login.example.com/mobile?code_challenge=def";
        when(oAuth2Service.generateAuthUrlForMobiles(anyString())).thenReturn(dummyUrl);
        
        mockMvc.perform(get("/api/auth/mobile-login"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.url").value(dummyUrl))
            // Also check that a codeVerifier is present.
            .andExpect(jsonPath("$.codeVerifier").isNotEmpty());
    }
}
