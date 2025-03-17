package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.services.AchievementsService;
import com.models.XboxProfileAchievements.Achievements;
import java.util.List;

public class AchievementsServiceTest {

    @Test
    public void testGetAchievements() {
        AchievementsService service = new AchievementsService();
        String sampleJson = "{ \"achievements\": [ { \"id\": \"ach1\", \"description\": \"First achievement\", \"progression\": { \"timeUnlocked\": \"2021-01-01T00:00:00Z\" }, \"mediaAssets\": [ { \"name\": \"asset1\", \"type\": \"image\", \"url\": \"http://example.com/asset.png\" } ], \"titleAssociations\": [ { \"name\": \"Game1\", \"id\": \"game1\" } ] } ] }";
        List<Achievements> achievements = service.getAchievements("xuid123", sampleJson);
        assertNotNull(achievements);
        assertFalse(achievements.isEmpty());
        assertEquals("ach1", achievements.get(0).getId());
    }
}
