package com.bluedelivery.shop.application.businesshour;

import java.util.List;
import java.util.Map;

import com.bluedelivery.shop.interfaces.dto.BusinessHourDay;
import com.bluedelivery.shop.application.dto.BusinessHourParam;
import com.bluedelivery.shop.domain.BusinessHour;

public interface BusinessHourCondition {
    
    boolean isSatisfied(BusinessHourType type, Map<BusinessHourDay, BusinessHourParam> params);
    
    List<BusinessHour> mapToDayOfWeek(Map<BusinessHourDay, BusinessHourParam> params);
}
