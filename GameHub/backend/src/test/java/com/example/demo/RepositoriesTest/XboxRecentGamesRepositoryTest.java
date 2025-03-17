package com.example.demo.RepositoriesTest;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.models.DataModelAccountLinks.XboxRecentGame;
import com.models.XboxModel.XboxProfile;
import com.Repository.XboxRecentGamesRepository;
import com.Repository.UserRepository;
import com.Repository.XboxProfileRepository;
import com.models.UserModel.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class XboxRecentGamesRepositoryTest {

    @Autowired
    private XboxRecentGamesRepository xboxRecentGamesRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private XboxProfileRepository xboxProfileRepository;

    @Test
    public void testFindRecentGamesByUsername() {
        // Create a user
        User user = new User();
        user.setUsername("xboxgamer");
        userRepository.save(user);
        
        // Create and save an XboxProfile for that user
        XboxProfile profile = new XboxProfile();
        profile.setUser(user);
        profile.setXboxGamertag("GamerTag");
        xboxProfileRepository.save(profile);
        
        // Create a dummy recent game record
        XboxRecentGame recentGame = new XboxRecentGame();
        recentGame.setXboxProfile(profile);
        recentGame.setGameName("Test Game");
        recentGame.setTitleId("title123");
        recentGame.setDisplayImage("http://example.com/image.png");
        recentGame.setLastTimePlayedFormatted("2021-01-01T00:00:00Z");  // use full ISO string
        xboxRecentGamesRepository.save(recentGame);
        
        List<XboxRecentGame> recentGames = xboxRecentGamesRepository.findRecentGamesByUsername("xboxgamer");
        assertThat(recentGames).isNotEmpty();
        assertThat(recentGames.get(0).getGameName()).isEqualTo("Test Game");
    }
}
