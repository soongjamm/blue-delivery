package com.delivery.user;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    void save(User user);

    User findByEmail(String email);

    User loadUser(LoginDto loginDto);
}
