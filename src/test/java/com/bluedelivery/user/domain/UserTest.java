package com.bluedelivery.user.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bluedelivery.authentication.application.AuthenticationFailedException;

class UserTest {
    
    User user;
    String email;
    String password;
    
    @BeforeEach
    void setup() {
        email = "my@gmail.com";
        password = "P@ssw0rd";
        user = new User(email, "nickname", "", password, LocalDate.now());
    }
    
    @Test
    void throwExceptionIfPasswordIsNotEqual() {
        assertThrows(AuthenticationFailedException.class, () -> user.validate("Wrong Password"));
    }
}
