package com.bluedelivery.shop.domain.holiday;

import java.time.LocalDate;

public interface HolidayPolicy {

    boolean isHoliday(LocalDate localDate);

}
