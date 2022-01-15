package com.bluedelivery.user.interfaces.adapter;

import com.bluedelivery.user.domain.User;

import lombok.Getter;

@Getter
public class CreatedUserDto {
    private String email;
    private String nickname;

    public CreatedUserDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
