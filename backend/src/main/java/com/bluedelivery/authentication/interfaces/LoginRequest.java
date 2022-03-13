package com.bluedelivery.authentication.interfaces;

import com.bluedelivery.authentication.application.LoginTarget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    private String email;
    private String password;
    
    public LoginTarget toParam() {
        return new LoginTarget(email, password);
    }
}
