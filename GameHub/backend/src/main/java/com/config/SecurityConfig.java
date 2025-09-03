// package com.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.context.annotation.Bean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//     @Autowired
//     private AuthenticationConfiguration authenticationConfiguration;

//     @Bean
//     public AuthenticationManager authenticationManager() throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests()
//                 .requestMatchers("/api/auth/**", "/api/xbox/**", "/api/news/**", "/api/community-insight/**", "/api/search/**", "/api/friends/**", "/api/save-games/**", "/api/game-trailers/**", "/steam/**" , "/api/steam/userinfo/**", "/api/user/linked-profiles/**", "/api/dm/**", "/api/user/**", "/ws/**").permitAll()
//                 .anyRequest().authenticated()
//             .and()
//             .cors()
//             .and()
//             .csrf().disable();
//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }

package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // enable CORS and use the CorsFilter bean you already created
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // explicitly allow preflight requests to pass through
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // permit all your public endpoints
                .requestMatchers(
                    "/api/auth/**", "/api/xbox/**", "/api/news/**",
                    "/api/community-insight/**", "/api/search/**",
                    "/api/friends/**", "/api/save-games/**", "/api/game-trailers/**",
                    "/steam/**", "/api/steam/userinfo/**",
                    "/api/user/linked-profiles/**", "/api/dm/**", "/api/user/**", "/ws/**"
                ).permitAll()

                // everything else needs authentication
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
