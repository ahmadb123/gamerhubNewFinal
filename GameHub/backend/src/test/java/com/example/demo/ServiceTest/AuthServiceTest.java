package com.example.demo.ServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.services.AuthService;
import com.services.UserDetailsService.UserService;
import com.utility.JWT;
import com.Repository.UserRepository;
import com.models.UserModel.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthServiceTest {

    private AuthService authService;
    private UserService userService;
    private JWT jwt;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        authService = new AuthService();
        userService = mock(UserService.class);
        jwt = new JWT();
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        
        try {
            java.lang.reflect.Field field;
            field = AuthService.class.getDeclaredField("userService");
            field.setAccessible(true);
            field.set(authService, userService);
            field = AuthService.class.getDeclaredField("jwt");
            field.setAccessible(true);
            field.set(authService, jwt);
            field = AuthService.class.getDeclaredField("passwordEncoder");
            field.setAccessible(true);
            field.set(authService, passwordEncoder);
            field = AuthService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(authService, userRepository);
        } catch(Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testAuthenticate_InvalidPassword() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("testuser")
            .password("encodedPassword").build();
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticate("testuser", "wrongPassword");
        });
        assertTrue(exception.getMessage().contains("Invalid credentials"));
    }
    
    @Test
    public void testRegister_UserAlreadyExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(new User()));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register("testuser", "test@example.com", "password");
        });
        assertEquals("User already exists", exception.getMessage());
    }
}
