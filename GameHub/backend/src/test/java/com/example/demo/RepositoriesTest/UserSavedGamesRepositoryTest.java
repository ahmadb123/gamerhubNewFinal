package com.example.demo.RepositoriesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.models.UserSavedGames.MyGames;
import com.models.UserModel.User;
import com.Repository.UserSavedGamesRepository;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserSavedGamesRepositoryTest {

    @Autowired
    private UserSavedGamesRepository userSavedGamesRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindGameIdsByUserId() {
        // Create and persist a user
        User user = new User();
        user.setUsername("gamer1");
        userRepository.save(user);
        
        // Create and persist a game saved record
        MyGames game = new MyGames();
        game.setUser(user);
        game.setGameId(12345L);
        userSavedGamesRepository.save(game);
        
        // Query for game IDs by user ID
        List<Long> gameIds = userSavedGamesRepository.findGameIdsByUserId(user.getId());
        assertThat(gameIds).contains(12345L);
    }
}
