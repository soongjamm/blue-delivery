package com.bluedelivery.authentication.interfaces;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bluedelivery.authentication.application.AuthenticationService;
import com.bluedelivery.authentication.domain.Authentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {
    
    private final AuthenticationService authenticationService;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getType().isAssignableFrom(Authentication.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        if (AuthenticationHolder.hasAuthentication()) {
            return AuthenticationHolder.getAuthentication();
        }
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        Optional<Authentication> optional = authenticationService.getAuthentication(req.getHeader(AUTHORIZATION));
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
    
}
