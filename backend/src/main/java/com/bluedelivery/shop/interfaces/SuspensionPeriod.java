package com.bluedelivery.shop.interfaces;

import java.time.LocalDateTime;

public enum SuspensionPeriod {
    NONE(0, 0),
    HALF_HOUR(0, 30),
    ONE_HOUR(1, 0),
    ONE_HOUR_AND_HALF(1, 30),
    TWO_HOURS(2, 0),
    THREE_HOURS(3, 0),
    FOUR_HOURS(4, 0);

    private final int hours;
    private final int minutes;

    SuspensionPeriod(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public LocalDateTime from(LocalDateTime now) {
        return now.plusHours(hours).plusMinutes(minutes);
    }
}
