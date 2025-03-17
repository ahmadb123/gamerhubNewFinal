package com.example.demo.ServiceTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import com.services.UserDetailsService.UserService;
import com.Repository.UserRepository;
import com.models.UserModel.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceTest {

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService();
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(userService, userRepository);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
        when(userRepository.findByUsername("nonexistent")).thenReturn(java.util.Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
    }
}
