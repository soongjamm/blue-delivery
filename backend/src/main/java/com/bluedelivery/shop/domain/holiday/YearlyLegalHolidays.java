package com.bluedelivery.shop.domain.holiday;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YearlyLegalHolidays {
    private final Map<Year, Set<LocalDate>> yearly;
    
    public YearlyLegalHolidays() {
        this.yearly = new HashMap<>();
        updateHolidaysIfNotExist(Year.of(LocalDate.now().getYear()));
    }
    
    public boolean isHoliday(LocalDate date) {
        Year year = Year.of(date.getYear());
        updateHolidaysIfNotExist(year);
        Set<LocalDate> holidaysOfAYear = yearly.get(year);
        return holidaysOfAYear.contains(date);
    }
    
    private synchronized void updateHolidaysIfNotExist(Year year) {
        if (yearly.containsKey(year)) {
            return;
        }
        yearly.put(year, LegalHoliday.getSolarDays(year));
    }
}
