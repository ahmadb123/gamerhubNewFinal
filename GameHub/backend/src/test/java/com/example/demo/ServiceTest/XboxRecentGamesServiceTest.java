package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.services.XboxRecentGamesService;
import com.models.XboxModel.RecentGamesXbox;
import com.models.XboxModel.TitleId;
import com.models.XboxModel.GameLastPlayed;
import com.models.XboxModel.XboxProfile;
import com.models.DataModelAccountLinks.XboxRecentGame;
import com.Repository.XboxRecentGamesRepository;

public class XboxRecentGamesServiceTest {

    private XboxRecentGamesService service;
    private XboxRecentGamesRepository repository;

    @BeforeEach
    public void setUp() {
        service = new XboxRecentGamesService();
        repository = mock(XboxRecentGamesRepository.class);
        try {
            java.lang.reflect.Field field = XboxRecentGamesService.class.getDeclaredField("xboxRecentGamesRepository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testSaveRecentGames() {
        RecentGamesXbox recentGames = new RecentGamesXbox();
        TitleId title = new TitleId();
        title.setTitleId("title1");
        title.setName("Test Game");
        title.setDisplayImage("http://example.com/display.png");
        GameLastPlayed history = new GameLastPlayed();
        history.setLastTimePlayed("2021-01-01T00:00:00Z");
        title.setTitleHistory(history);
        recentGames.setTitles(Arrays.asList(title));

        XboxProfile profile = new XboxProfile();
        profile.setId(100L);

        service.saveRecentGames(recentGames, profile);
        verify(repository, atLeastOnce()).save(any(XboxRecentGame.class));
    }
}
