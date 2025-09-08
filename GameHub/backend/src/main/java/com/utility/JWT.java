
// package com.utility;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.beans.factory.InitializingBean;
// import javax.crypto.SecretKey;
// import java.util.Date;

// @Component
// public class JWT implements InitializingBean {

//     @Value("${jwt.secret}")
//     private String secret;

//     @Value("${jwt.expiration}")
//     private long expirationTimeInMillis;

//     private SecretKey SECRET_KEY;

//     @Override
//     public void afterPropertiesSet() {
//         byte[] decodeKey = Decoders.BASE64.decode(secret);
//         SECRET_KEY = Keys.hmacShaKeyFor(decodeKey);
//     }

//     // Generate JWT token for a regular user
//     public String generateToken(Long userId, String username) {
//         return Jwts.builder()
//                 .setSubject(username)
//                 .claim("userId", userId)  
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
//                 .signWith(SECRET_KEY)
//                 .compact();
//     }

//     // Generate JWT token for guest
//     public String generateGuestToken() {
//         long guestExpiration = 1800000L; // 30 minutes
//         return Jwts.builder()
//                 .setSubject("guest")
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + guestExpiration))
//                 .signWith(SECRET_KEY)
//                 .compact();
//     }

//     // Extract username from JWT token
//     public String extractUsername(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(SECRET_KEY)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getSubject();
//     }

//     public Long extractUserId(String token){
//         return Jwts.parserBuilder()
//             .setSigningKey(SECRET_KEY)
//             .build()
//             .parseClaimsJws(token)
//             .getBody()
//             .get("userId", Long.class);
//     }

//     // Validate the JWT token
//     public boolean isTokenValid(String token, String userName) {
//         String extractedUser = extractUsername(token);
//         return extractedUser.equals(userName) && !isTokenExpired(token);
//     }

//     // Check if the token is expired
//     private boolean isTokenExpired(String token) {
//         Date expiration = Jwts.parserBuilder()
//                 .setSigningKey(SECRET_KEY)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getExpiration();
//         return expiration.before(new Date());
//     }
// }



package com.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWT implements InitializingBean {

    @Value("${jwt.secret}")
    private String secretValue; // from env var JWT_SECRET (Base64 recommended)

    @Value("${jwt.expiration:86400000}") // 24h default
    private long expirationMs;

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes;

        // Try Base64 first (recommended)
        try {
            keyBytes = Decoders.BASE64.decode(secretValue);
        } catch (IllegalArgumentException ex) {
            // Fallback: treat as raw text
            keyBytes = secretValue.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 32) { // 256 bits
            throw new IllegalStateException(
                "JWT secret must be at least 256 bits (32 bytes). Provided: " + (keyBytes.length * 8) + " bits."
            );
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ---- Token creation ----
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateGuestToken() {
        long guestExpirationMs = 30 * 60 * 1000L; // 30 minutes
        Date now = new Date();

        return Jwts.builder()
                .setSubject("guest")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + guestExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---- Parsing helpers ----
    private Claims parseClaims(String token) throws JwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = parseClaims(token);
            boolean notExpired = claims.getExpiration().after(new Date());
            boolean subjectMatches = expectedUsername != null && expectedUsername.equals(claims.getSubject());
            return notExpired && subjectMatches;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
