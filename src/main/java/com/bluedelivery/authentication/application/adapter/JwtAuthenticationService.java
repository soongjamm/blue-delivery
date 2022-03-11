package com.bluedelivery.authentication.application.adapter;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bluedelivery.authentication.application.AuthenticationService;
import com.bluedelivery.authentication.application.LoginTarget;
import com.bluedelivery.authentication.domain.Authentication;
import com.bluedelivery.authentication.domain.TokenType;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.domain.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtAuthenticationService implements AuthenticationService {
    
    private final Key jwtSigningKey;
    private final Date jwtValidUntil;
    private final UserRepository userRepository;
    
    public JwtAuthenticationService(Key jwtSigningKey, Date jwtValidUntil, UserRepository userRepository) {
        this.jwtSigningKey = jwtSigningKey;
        this.jwtValidUntil = jwtValidUntil;
        this.userRepository = userRepository;
    }
    
    @Override
    public Optional<Authentication> getAuthentication(String authenticationHeader) {
        try {
            String token = TokenType.BEARER.extract(authenticationHeader);
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(jwtSigningKey)
                    .build()
                    .parseClaimsJws(token);
            Long userId = Long.parseLong((String)jws.getBody().get("userId"));
            return Optional.of(new Authentication(token, userId));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Authentication authenticate(LoginTarget target) {
        User user = userRepository.findByEmail(target.getEmail()).orElseThrow();
        user.validate(target.getPassword());
        return new Authentication(createJws(user), user.getId());
    }
    
    private String createJws(User user) {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> payloads = new HashMap<>();
        
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        payloads.put("userId", user.getId().toString());
        
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user")
                .setExpiration(jwtValidUntil)
                .signWith(jwtSigningKey)
                .compact();
    }
    
    @Override
    public void expire(Authentication loggedIn) {
        throw new UnsupportedOperationException("JWT는 토큰 폐기가 불가능");
    }
}
