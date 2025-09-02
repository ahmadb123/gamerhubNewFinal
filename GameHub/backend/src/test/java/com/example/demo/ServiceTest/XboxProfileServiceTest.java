package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;

import com.dto.XboxProfileDTO;
import com.services.TokenService;
import com.services.XboxProfileService;

public class XboxProfileServiceTest {

    @Test
    public void testParseProfileJson() throws Exception {
        XboxProfileService service = new XboxProfileService();
        TokenService tokenService = mock(TokenService.class);
        try {
            java.lang.reflect.Field field = XboxProfileService.class.getDeclaredField("tokenService");
            field.setAccessible(true);
            field.set(service, tokenService);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
        String jsonResponse = "{ \"profileUsers\": [ { \"id\": \"xuid123\", \"settings\": [ { \"id\": \"Gamertag\", \"value\": \"TestGamer\" }, { \"id\": \"GameDisplayName\", \"value\": \"TestGame\" }, { \"id\": \"AppDisplayPicRaw\", \"value\": \"http://example.com/app.png\" }, { \"id\": \"GameDisplayPicRaw\", \"value\": \"http://example.com/game.png\" }, { \"id\": \"AccountTier\", \"value\": \"Gold\" }, { \"id\": \"TenureLevel\", \"value\": \"10\" }, { \"id\": \"Gamerscore\", \"value\": \"1500\" } ] } ] }";
        XboxProfileDTO dto = service.parseProfileJson(jsonResponse);
        assertNotNull(dto);
        assertEquals("xuid123", dto.getId());
        assertEquals("TestGamer", dto.getGamertag());
        assertEquals("TestGame", dto.getGameDisplayName());
        assertEquals("Gold", dto.getAccountTier());
        assertEquals(10, dto.getTenureLevel());
        assertEquals(1500, dto.getGamerscore());
    }
}
