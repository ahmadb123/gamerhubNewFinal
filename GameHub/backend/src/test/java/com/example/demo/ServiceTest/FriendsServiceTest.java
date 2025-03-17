package com.example.demo.ServiceTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.services.FriendsService;
import com.Repository.FriendsRepository;
import com.models.FriendsModel.Friends;
import com.models.UserModel.User;

public class FriendsServiceTest {

    private FriendsRepository friendsRepository;
    private FriendsService friendsService;

    @BeforeEach
    public void setUp() {
        friendsRepository = mock(FriendsRepository.class);
        friendsService = new FriendsService();
        // Inject the mocked repository using reflection.
        try {
            java.lang.reflect.Field field = FriendsService.class.getDeclaredField("friendsRepository");
            field.setAccessible(true);
            field.set(friendsService, friendsRepository);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testSendFriendRequest() {
        User requester = new User();
        requester.setId(1L);
        requester.setUsername("requester");
        User target = new User();
        target.setId(2L);
        target.setUsername("target");

        friendsService.sendFriendRequest(requester, target);
        // Verify that the save method is invoked.
        verify(friendsRepository, times(1)).save(any(Friends.class));
    }

    @Test
    public void testAcceptFriendRequest() {
        Friends friend = new Friends();
        friend.setId(10L);
        friend.setStatus("pending");
        friend.setCreatedAt(LocalDateTime.now());
        when(friendsRepository.findById(10L)).thenReturn(Optional.of(friend));

        friendsService.acceptFriendRequest(10L);
        assertEquals("accepted", friend.getStatus());
        verify(friendsRepository, times(1)).save(friend);
    }

    @Test
    public void testGetAllFriends() {
        // Create a dummy friend relationship.
        Friends friend = new Friends();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        friend.setUser(user1);
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        friend.setFriend(user2);
        friend.setStatus("accepted");

        List<Friends> friendsList = new ArrayList<>();
        friendsList.add(friend);
        when(friendsRepository.findAllAcceptedByUserId(1L)).thenReturn(friendsList);

        List<User> result = friendsService.getAllFriends(1L);
        assertNotNull(result);
        // Based on the logic, the returned friend should be user2.
        assertEquals(1, result.size());
        assertEquals("user2", result.get(0).getUsername());
    }
}
