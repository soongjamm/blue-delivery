package com.bluedelivery.shop.domain.holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.function.Predicate;

import static java.time.YearMonth.from;

public enum WeekCycle {

    FIRST(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == 1),
    SECOND(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == 2),
    THIRD(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == 3),
    FOURTH(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == 4),
    FIFTH(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == 5),
    LAST(date -> date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()) == from(date).atEndOfMonth().get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()));

    private final Predicate<LocalDate> function;

    WeekCycle(Predicate<LocalDate> function) {
        this.function = function;
    }

    public boolean match(LocalDate localDate) {
        return function.test(localDate);
    }
}
