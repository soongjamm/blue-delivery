package com.bluedelivery.shop.domain.holiday;

import java.time.LocalDate;

public class LegalHolidayPolicy implements HolidayPolicy {
    @Override
    public boolean isHoliday(LocalDate localDate) {
        return new YearlyLegalHolidays().isHoliday(localDate);
    }
}
