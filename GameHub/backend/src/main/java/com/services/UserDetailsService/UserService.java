package com.services.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Repository.UserRepository;
import com.models.UserModel.User;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername()) // Adjust to match getter name
        .password(user.getPassword()) // hashed password
        .build();
    } 

    // update/ser userbio 
    public String updateUserBio(Long userId, String bio){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUserBio(bio);
        userRepository.save(user);
        return user.getUserBio(); // now returns the updated bio
    }

    // get user bio
    public String getUserBio(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String bio = user.getUserBio();
        if(bio == null || bio.isEmpty()){
            throw new RuntimeException("Bio not found");
        } else {
            return bio;
        }
    }

    // update username upon user request - 
    public String updateUsername(Long userId, String newUsername){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(newUsername);
        userRepository.save(user);
        return user.getUsername(); // now returns the updated username
    }

    // update password 
    public String updatePassword(Long userId, String newPassword){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return user.getPassword(); // now returns the updated password
    }
}
