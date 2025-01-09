
package com.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWT implements InitializingBean {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTimeInMillis;

    private SecretKey SECRET_KEY;

    @Override
    public void afterPropertiesSet() {
        byte[] decodeKey = Decoders.BASE64.decode(secret);
        SECRET_KEY = Keys.hmacShaKeyFor(decodeKey);
    }

    // Generate JWT token for a regular user
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)  
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Generate JWT token for guest
    public String generateGuestToken() {
        long guestExpiration = 1800000L; // 30 minutes
        return Jwts.builder()
                .setSubject("guest")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + guestExpiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long extractUserId(String token){
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId", Long.class);
    }

    // Validate the JWT token
    public boolean isTokenValid(String token, String userName) {
        String extractedUser = extractUsername(token);
        return extractedUser.equals(userName) && !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}


