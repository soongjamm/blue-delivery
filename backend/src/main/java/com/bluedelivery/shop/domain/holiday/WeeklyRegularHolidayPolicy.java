package com.bluedelivery.shop.domain.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeeklyRegularHolidayPolicy implements HolidayPolicy {

    private final DayOfWeek dayOfWeek;

    public WeeklyRegularHolidayPolicy(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return this.dayOfWeek == date.getDayOfWeek();
    }
}
