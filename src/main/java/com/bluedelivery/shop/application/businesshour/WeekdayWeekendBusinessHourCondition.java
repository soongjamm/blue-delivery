package com.bluedelivery.shop.application.businesshour;

import static com.bluedelivery.shop.interfaces.dto.BusinessHourDay.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bluedelivery.shop.application.dto.BusinessHourParam;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.interfaces.dto.BusinessHourDay;


public class WeekdayWeekendBusinessHourCondition implements BusinessHourCondition {
    
    @Override
    public boolean isSatisfied(BusinessHourType type, Map<BusinessHourDay, BusinessHourParam> hours) {
        if (type == BusinessHourType.WEEKDAY_SAT_SUNDAY
                && hours.size() == 3
                && hasRequiredDays(hours)) {
            return true;
        }
        return false;
    }
    
    @Override
    public List<BusinessHour> mapToDayOfWeek(Map<BusinessHourDay, BusinessHourParam> hours) {
        List<BusinessHour> bhs = new ArrayList<>();
        BusinessHourParam weekday = hours.get(WEEKDAY);
        BusinessHourParam saturday = hours.get(SATURDAY);
        BusinessHourParam sunday = hours.get(SUNDAY);
        
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.compareTo(DayOfWeek.SATURDAY) >= 0) {
                break;
            }
            bhs.add(weekday.toEntity(day));
        }
        bhs.add(saturday.toEntity(DayOfWeek.SATURDAY));
        bhs.add(sunday.toEntity(DayOfWeek.SUNDAY));
    
        return bhs;
    }
    
    private boolean hasRequiredDays(Map<BusinessHourDay, BusinessHourParam> hours) {
        return hours.containsKey(WEEKDAY)
                && hours.containsKey(SATURDAY)
                && hours.containsKey(SUNDAY);
    }
}
