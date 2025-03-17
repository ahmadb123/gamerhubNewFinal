package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.services.SaveGamesService;
import com.Repository.UserSavedGamesRepository;
import com.models.NewsModel.NewsResults;
import com.models.UserModel.User;

public class SaveGamesServiceTest {

    private SaveGamesService service;
    private UserSavedGamesRepository repository;

    @BeforeEach
    public void setUp() {
        service = new SaveGamesService();
        repository = mock(UserSavedGamesRepository.class);
        try {
            java.lang.reflect.Field field = SaveGamesService.class.getDeclaredField("userSavedGamesRepository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testSaveGame() {
        User user = new User();
        user.setId(1L);
        NewsResults game = new NewsResults();
        game.setId(100L);
        service.saveGame(user, game);
        verify(repository, times(1)).save(any());
    }
}
