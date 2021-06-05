package com.delivery.restaurant.businesshour;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class BusinessHourPolicy {
    protected List<BusinessHour> businessHours;
    
    public BusinessHourPolicy() {
        this.businessHours = new ArrayList<>();
    }
    
    public void setup(List<BusinessHour> bhs) {
        this.businessHours = bhs;
    }
    
    public void update(DayOfWeek day, BusinessHour businessHour) {
        businessHour.updateDayOfWeek(day);
        businessHours.add(businessHour);
    }
    
    public List<BusinessHour> getBusinessHours() {
        return businessHours;
    }
    
    public BusinessHour getBusinessHourOf(DayOfWeek day) {
        return businessHours.stream()
                .filter(x -> x.getDayOfWeek() == day)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("dayOfWeek does not exist"));
    }
    
    public BusinessHourResponse toResponse() {
        return new BusinessHourResponse(this.businessHours);
    }
}
