package com.controllers.LoginController;

import java.beans.BeanProperty;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AuthResponseDTO;
import com.dto.UserDTO;
import com.services.AuthService;
import com.utility.JWT;

@RestController
@RequestMapping("/api/auth")
public class Authentication {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWT jwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDto){
        String token = authService.authenticate(userDto.getUsername(), userDto.getPassword());
        Long userId = jwt.extractUserId(token);
        String username = jwt.extractUsername(token);
        return ResponseEntity.ok(new AuthResponseDTO(token, userId, username)); // call construction
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDto){
        try{
            authService.register(userDto.getUsername(),userDto.getEmail(),userDto.getPassword());
            return ResponseEntity.ok("User registered successfully");
        }catch(RuntimeException e){
            if (e.getMessage().equals("User already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
