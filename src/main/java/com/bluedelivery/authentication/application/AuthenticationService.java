package com.bluedelivery.authentication.application;

import java.util.Optional;

import com.bluedelivery.authentication.domain.Authentication;

public interface AuthenticationService {
    Optional<Authentication> getAuthentication(String authenticationHeader);
    
    Authentication authenticate(LoginTarget loginDto);
    
    void expire(Authentication loggedIn);
    
}
