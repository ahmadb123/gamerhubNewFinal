package com.example.demo.UtilityTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.utility.XboxProfileMapper;
import com.dto.XboxProfileDTO;
import com.models.XboxModel.XboxProfile;
import com.models.UserModel.User;

public class XboxProfileMapperTest {

    @Test
    public void testToEntityAndToDTO() {
        // Create a sample DTO
        XboxProfileDTO dto = new XboxProfileDTO();
        dto.setId("12345");
        dto.setGamertag("TestGamer");
        dto.setAppDisplayName("TestApp");
        dto.setGameDisplayName("TestGame");
        dto.setAppDisplayPicRaw("pic1.png");
        dto.setGameDisplayPicRaw("pic2.png");
        dto.setAccountTier("Gold");
        dto.setTenureLevel(5);
        dto.setGamerscore(1500);
        
        // Create a sample user
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        
        // Map DTO to entity
        XboxProfile entity = XboxProfileMapper.toEntity(dto, user);
        assertNotNull(entity, "Entity should not be null");
        assertEquals("12345", entity.getXboxId(), "Xbox ID should match");
        assertEquals("TestGamer", entity.getXboxGamertag(), "Gamertag should match");
        
        // Map entity back to DTO
        XboxProfileDTO mappedDTO = XboxProfileMapper.toDTO(entity);
        assertNotNull(mappedDTO, "Mapped DTO should not be null");
        assertEquals(entity.getXboxId(), mappedDTO.getId(), "ID should match between entity and DTO");
    }
}
