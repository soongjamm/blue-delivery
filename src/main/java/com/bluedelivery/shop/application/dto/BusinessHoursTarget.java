package com.bluedelivery.shop.application.dto;

import java.util.Map;

import com.bluedelivery.shop.interfaces.dto.BusinessHourDay;
import com.bluedelivery.shop.application.businesshour.BusinessHourType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessHoursTarget {
    private Long shopId;
    private BusinessHourType businessHourType;
    private Map<BusinessHourDay, BusinessHourParam> businessHours;
}
