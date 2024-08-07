package com.bluedelivery.user.interfaces.adapter;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bluedelivery.user.interfaces.UserRegisterRequest;

@Component
public class PasswordValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserRegisterRequest.class);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterRequest dto = (UserRegisterRequest) target;
        if (!Objects.equals(dto.getPassword(), dto.getConfirmedPassword())) {
            errors.rejectValue("password", "", "패스워드 불일치");
        }
    }
}
