package com.bluedelivery.shop.domain.holiday;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
public class CyclicRegularHolidayPolicy implements HolidayPolicy {

    private final WeekCycle weekCycle;
    private final DayOfWeek dayOfWeek;

    public CyclicRegularHolidayPolicy(WeekCycle weekCycle, DayOfWeek dayOfWeek) {
        this.weekCycle = weekCycle;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return weekCycle.match(date) && dayOfWeek == date.getDayOfWeek();
    }

}
