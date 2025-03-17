package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; // added import for eq
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.controllers.XboxController.XboxProfileController;
import com.dto.XboxProfileDTO;
import com.models.UserModel.User;
import com.models.XboxModel.XboxProfile;
import com.Repository.UserRepository;
import com.services.TokenService;
import com.services.XboxProfileService;
import com.utility.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@AutoConfigureMockMvc(addFilters = false)

@WebMvcTest(controllers = XboxProfileController.class)
public class XboxProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private XboxProfileService xboxProfileService;
    
    @MockBean
    private JWT jwt;
    
    @MockBean
    private com.Repository.UserRepository userRepository;
    
    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;
    
    @Test
    public void testGetProfile_Success() throws Exception {
        // Stub the JWT extraction
        when(jwt.extractUsername("dummyToken")).thenReturn("testuser");
        // Stub user lookup
        User dummyUser = new User();
        dummyUser.setId(1L);
        dummyUser.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(dummyUser));
        
        // Stub TokenService to return a dummy Xbox auth header
        when(tokenService.getXboxAuthorizationHeader()).thenReturn("XBL3.0 x=dummyhash;dummyToken");
        // Stub RestTemplate to return a dummy JSON string with type-safety adjustments
        String dummyJson = "{\"dummy\":\"value\"}";
        when(restTemplate.exchange(any(String.class), any(), any(), eq(String.class)))
            .thenReturn((ResponseEntity<String>) ResponseEntity.ok(dummyJson));
        
        // Stub xboxProfileService.parseProfileJson to return a dummy DTO
        XboxProfileDTO dummyDto = new XboxProfileDTO();
        dummyDto.setId("dummyXuid");
        dummyDto.setGamertag("TestGamer");
        when(xboxProfileService.parseProfileJson(dummyJson)).thenReturn(dummyDto);
        
        // When saving profile, we can simply return the DTO.
        XboxProfile dummyProfile = new XboxProfile();
        dummyProfile.setId(1L); // Assuming 1L is the correct Long value for the dummy profile
        dummyProfile.setXboxGamertag("TestGamer");
        when(xboxProfileService.saveProfile(dummyDto, dummyUser)).thenReturn(dummyProfile);
        
        mockMvc.perform(get("/api/xbox/profile")
                .header("Authorization", "Bearer dummyToken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("dummyXuid"))
            .andExpect(jsonPath("$.gamertag").value("TestGamer"));
    }
}
