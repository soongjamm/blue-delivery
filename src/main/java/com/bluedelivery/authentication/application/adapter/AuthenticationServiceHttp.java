package com.bluedelivery.authentication.application.adapter;

import java.util.Optional;
import java.util.UUID;

import com.bluedelivery.authentication.application.LoginTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.authentication.application.AuthenticationService;
import com.bluedelivery.authentication.domain.Authentication;
import com.bluedelivery.authentication.domain.AuthenticationRepository;
import com.bluedelivery.authentication.domain.TokenType;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthenticationServiceHttp implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;
    
    @Override
    public Optional<Authentication> getAuthentication(String authorization) {
        String token = TokenType.BEARER.extract(authorization);
        return authenticationRepository.findByToken(token);
    }
    
    @Override
    public Authentication authenticate(LoginTarget target) {
        User user = userRepository.findByEmail(target.getEmail()).orElseThrow();
        user.validate(target.getPassword());
        Authentication auth = new Authentication(UUID.randomUUID().toString(), user.getId());
        authenticationRepository.save(auth);
        return auth;
    }
    
    @Override
    public void expire(Authentication loggedIn) {
        authenticationRepository.expire(loggedIn);
    }
}
