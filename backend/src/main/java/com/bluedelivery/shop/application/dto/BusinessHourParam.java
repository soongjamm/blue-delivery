package com.bluedelivery.shop.application.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.bluedelivery.shop.domain.BusinessHour;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessHourParam {
    private LocalTime open;
    private LocalTime close;
    
    public BusinessHour toEntity(DayOfWeek dayOfWeek) {
        return new BusinessHour(dayOfWeek, open, close);
    }
}
