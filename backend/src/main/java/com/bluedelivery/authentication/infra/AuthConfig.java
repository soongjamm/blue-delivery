package com.bluedelivery.authentication.infra;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.bluedelivery.authentication.application.AuthenticationService;
import com.bluedelivery.authentication.application.adapter.JwtAuthenticationService;
import com.bluedelivery.user.domain.UserRepository;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Configuration
public class AuthConfig {
    
    private final UserRepository userRepository;
    
    public AuthConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Bean
    @Primary
    public AuthenticationService authenticationService(Key jwtSigningKey, Date jwtValidUntil) {
        return new JwtAuthenticationService(jwtSigningKey, jwtValidUntil, userRepository);
    }
    
    @Bean
    public Key jwtSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    
    @Bean
    public Date jwtValidUntil() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }
}
