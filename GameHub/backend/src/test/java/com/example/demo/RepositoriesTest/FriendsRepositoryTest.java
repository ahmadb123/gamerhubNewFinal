package com.example.demo.RepositoriesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import com.models.FriendsModel.Friends;
import com.models.UserModel.User;
import com.Repository.FriendsRepository;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FriendsRepositoryTest {

    @Autowired
    private FriendsRepository friendsRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllAcceptedByUserId() {
        // Create two users
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);
        
        // Create a friend relationship
        Friends friend = new Friends();
        friend.setUser(user1);
        friend.setFriend(user2);
        friend.setStatus("accepted");
        friend.setCreatedAt(LocalDateTime.now());
        friendsRepository.save(friend);
        
        // Now fetch accepted friends for user1
        List<Friends> acceptedFriends = friendsRepository.findAllAcceptedByUserId(user1.getId());
        assertThat(acceptedFriends).isNotEmpty();
    }
}
