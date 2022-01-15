package com.bluedelivery.shop.interfaces.dto;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.bluedelivery.shop.application.businesshour.BusinessHourType;
import com.bluedelivery.shop.application.dto.BusinessHourParam;
import com.bluedelivery.shop.application.dto.BusinessHoursTarget;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessHoursRequest {
    
    @NotNull
    private BusinessHourType businessHourType;
    @NotNull
    private Map<BusinessHourDay, BusinessHourParam> businessHours;
    
    public BusinessHoursTarget toTarget(Long shopId) {
        return new BusinessHoursTarget(shopId, businessHourType, businessHours);
    }
}
