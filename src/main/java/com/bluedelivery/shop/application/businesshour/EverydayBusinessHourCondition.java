package com.bluedelivery.shop.application.businesshour;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bluedelivery.shop.application.dto.BusinessHourParam;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.interfaces.dto.BusinessHourDay;

public class EverydayBusinessHourCondition implements BusinessHourCondition {
    
    @Override
    public boolean isSatisfied(BusinessHourType type, Map<BusinessHourDay, BusinessHourParam> hours) {
        if (type == BusinessHourType.EVERY_SAME_TIME
                && hours.size() == 1
                && hours.containsKey(BusinessHourDay.EVERY_DAY)) {
            return true;
        }
        return false;
    }
    
    @Override
    public List<BusinessHour> mapToDayOfWeek(Map<BusinessHourDay, BusinessHourParam> hours) {
        List<BusinessHour> bhs = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            bhs.add(hours.get(BusinessHourDay.EVERY_DAY).toEntity(dayOfWeek));
        }
        return bhs;
    }
    
}
