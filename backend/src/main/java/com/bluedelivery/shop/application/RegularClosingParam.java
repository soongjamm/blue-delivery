package com.bluedelivery.shop.application;

import java.time.DayOfWeek;

import com.bluedelivery.shop.domain.holiday.CyclicRegularHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.HolidayPolicy;
import com.bluedelivery.shop.domain.holiday.WeekCycle;

import com.bluedelivery.shop.domain.holiday.WeeklyRegularHolidayPolicy;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegularClosingParam {
    
    private WeekCycle cycle;
    private DayOfWeek dayOfWeek;
    
    public RegularClosingParam(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public RegularClosingParam(WeekCycle cycle, DayOfWeek dayOfWeek) {
        this.cycle = cycle;
        this.dayOfWeek = dayOfWeek;
    }
    
    public HolidayPolicy toEntity() {
        if (this.cycle == null) {
            return new WeeklyRegularHolidayPolicy(this.dayOfWeek);
        }
        return new CyclicRegularHolidayPolicy(this.cycle, dayOfWeek);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    
    @Override
    public String toString() {
        return "RegularClosingParam{"
                + "cycle=" + cycle
                + ", dayOfWeek=" + dayOfWeek
                + '}';
    }
}
