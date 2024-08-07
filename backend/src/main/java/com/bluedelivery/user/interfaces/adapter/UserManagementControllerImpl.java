package com.bluedelivery.user.interfaces.adapter;

import static com.bluedelivery.authentication.domain.Authentication.AUTH_STR;
import static com.bluedelivery.common.response.HttpResponse.*;
import static org.springframework.http.HttpStatus.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import com.bluedelivery.address.domain.Address;
import com.bluedelivery.authentication.domain.Authentication;
import com.bluedelivery.common.response.HttpResponse;
import com.bluedelivery.user.application.UserManagementService;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.interfaces.AddAddressRequest;
import com.bluedelivery.user.interfaces.DeleteAccountRequest;
import com.bluedelivery.user.interfaces.UpdateAccountRequest;
import com.bluedelivery.user.interfaces.UserManagementController;
import com.bluedelivery.user.interfaces.UserRegisterRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserManagementControllerImpl implements UserManagementController {
    
    private final UserManagementService userManagementService;
    private final PasswordValidator passwordValidator;
    
    @InitBinder("userRegisterRequest")
    void initRegisterPasswordValidator(WebDataBinder binder) {
        binder.addValidators(passwordValidator);
    }
    
    public ResponseEntity<HttpResponse<Authentication>> getLoggedInUser(Authentication user) {
        return ResponseEntity.ok(response(user));
    }

    public ResponseEntity<?> deleteAccount(Long id, DeleteAccountRequest dto, HttpServletRequest request) {
        userManagementService.deleteAccount(dto.toParam(id));
        ((Authentication) request.getAttribute(AUTH_STR)).invalidate();
        return ResponseEntity.status(NO_CONTENT).build();
    }
    
    @Override
    public ResponseEntity<HttpResponse<?>> updateAccount(Long id, UpdateAccountRequest dto, HttpServletRequest req) {
        userManagementService.updateAccount(dto.toParam(id));
        return ResponseEntity.ok(response(SUCCESS, "successfully updated"));
    }
    
    @Override
    public ResponseEntity<HttpResponse<?>> addAddress(Long id, AddAddressRequest addressRequest) {
        Address address = userManagementService.addAddress(addressRequest.toParam(id));
        return ResponseEntity.status(CREATED).body(response(address));
    }
    
    @Override
    public ResponseEntity<HttpResponse<?>> setMainAddress(Long id, Long addressId) {
        boolean isSet = userManagementService.setMainAddress(id, addressId);
        if (isSet) {
            return ResponseEntity.ok(response(SUCCESS));
        }
        return ResponseEntity.badRequest().body(response(FAIL, "address id is wrong"));
    }
    
    @Override
    public ResponseEntity<?> removeAddress(Long id, Long addressId) {
        boolean removed = userManagementService.removeAddress(id, addressId);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @Override
    public ResponseEntity<?> register(UserRegisterRequest dto) {
        User user = userManagementService.register(dto.toParam());
        return ResponseEntity.status(CREATED).body(response(new CreatedUserDto(user)));
    }
}
