package com.example.demo.RepositoriesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.models.XboxModel.XboxProfile;
import com.models.UserModel.User;
import com.Repository.XboxProfileRepository;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class XboxProfileRepositoryTest {

    @Autowired
    private XboxProfileRepository xboxProfileRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserIdAndGamertag() {
        // Create a user and save
        User user = new User();
        user.setUsername("xboxuser");
        userRepository.save(user);
        
        // Create and save an XboxProfile
        XboxProfile profile = new XboxProfile();
        profile.setUser(user);
        profile.setXboxGamertag("GamerTag1");
        xboxProfileRepository.save(profile);
        
        Optional<XboxProfile> found = xboxProfileRepository.findByUserIdAndGamertag(user.getId(), "GamerTag1");
        assertThat(found).isPresent();
        assertThat(found.get().getXboxGamertag()).isEqualTo("GamerTag1");
    }
    
    @Test
    public void testFindAllByUserId() {
        User user = new User();
        user.setUsername("xboxuser");
        userRepository.save(user);
        
        XboxProfile profile1 = new XboxProfile();
        profile1.setUser(user);
        profile1.setXboxGamertag("GamerTag1");
        xboxProfileRepository.save(profile1);
        
        XboxProfile profile2 = new XboxProfile();
        profile2.setUser(user);
        profile2.setXboxGamertag("GamerTag2");
        xboxProfileRepository.save(profile2);
        
        List<XboxProfile> profiles = xboxProfileRepository.findAllByUserId(user.getId());
        assertThat(profiles).hasSize(2);
    }
}
