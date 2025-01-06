package com.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication; 
import com.Repository.UserRepository;
import com.models.UserModel.User;
import com.services.UserDetailsService.UserService;
import com.utility.JWT;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWT jwt;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private UserRepository userRepository;

    public String authenticate(String username, String password){
        // load user details - 
        UserDetails userDetails = userService.loadUserByUsername(username);

        // verify password - 
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        // authenticate user and generate jwt token - 
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // fetch username and userid from db -
        User userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

            return jwt.generateToken(userEntity.getId(), userEntity.getUsername());
        }

    public void register(String username, String email, String password){
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        // encrypt password and create user - 
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        // user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    // delete user by id
    public void deleteUser(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with id " + id + " does not exist.");
        }
        User user = optionalUser.get();
        userRepository.delete(user);
    }    
}
