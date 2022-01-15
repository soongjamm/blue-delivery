package com.bluedelivery.user.application.adapter;

import java.util.Locale;

import com.bluedelivery.user.application.AddAddressTarget;
import com.bluedelivery.user.application.UpdateAccountTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.user.application.DeleteAccountTarget;
import com.bluedelivery.user.application.UserManagementService;
import com.bluedelivery.user.application.UserRegisterTarget;
import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.common.response.ErrorCode;
import com.bluedelivery.address.domain.Address;
import com.bluedelivery.address.domain.AddressService;
import com.bluedelivery.address.domain.BuildingInfo;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserManagementServiceHttp implements UserManagementService {
    
    private final UserRepository userRepository;
    private final AddressService addressService;
    
    public User register(UserRegisterTarget dto) {
        try {
            return userRepository.save(dto.toEntity());
        } catch (RuntimeException ex) {
            String msg = ex.getMessage().toLowerCase(Locale.ROOT);
            if (msg.contains("email")) {
                throw new ApiException(ErrorCode.DUPLICATE_EMAIL);
            }
            if (msg.contains("nickname")) {
                throw new ApiException(ErrorCode.DUPLICATE_NICKNAME);
            }
            if (msg.contains("phone")) {
                throw new ApiException(ErrorCode.DUPLICATE_PHONE);
            }
            throw new IllegalArgumentException(ex);
        }
    }
    
    public User updateAccount(UpdateAccountTarget param) {
        User foundUser = getUserById(param.getId());
        foundUser.changePhone(param.getPhone());
        foundUser.changeNickname(param.getNickname());
        foundUser.setDateOfBirth(param.getDateOfBirth());
        return foundUser;
    }
    
    public void deleteAccount(DeleteAccountTarget param) {
        User user = getUserById(param.getId());
        user.validate(param.getPassword());
        userRepository.delete(user);
    }
    
    @Override
    public Address addAddress(AddAddressTarget param) {
        User user = getUserById(param.getId());
        BuildingInfo buildingInfo =
                addressService.getBuildingAddress(param.getBuildingManagementNumber());
        Address address = new Address(buildingInfo, param.getDetail());
        user.addAddress(address);
        return address;
    }
    
    @Override
    public boolean setMainAddress(Long userId, Long addressId) {
        User user = getUserById(userId);
        Address address = addressService.getAddress(addressId, user);
        return user.designateAsMainAddress(address);
    }
    
    @Override
    public boolean removeAddress(Long userId, Long addressId) {
        User user = getUserById(userId);
        Address address = addressService.getAddress(addressId, user);
        return user.removeAddress(address);
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }
    
}
