package com.bluedelivery.shop.domain.holiday;

import lombok.Getter;

import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.temporal.ChronoUnit.DAYS;

@Getter
public class TemporaryHolidayPolicy implements HolidayPolicy {

    private static final int MAX_SHUTDOWN_DAYS = 30;

    private final LocalDate from;
    private final LocalDate to;

    public TemporaryHolidayPolicy(LocalDate from, LocalDate to) {
        checkArgument(from.isEqual(to) || from.isBefore(to), "휴무 시작일은 종료일보다 과거이거나 같아야 합니다.");
        checkArgument(DAYS.between(from, to) <= MAX_SHUTDOWN_DAYS, "휴무는 30일 이하여야 합니다.");
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean isHoliday(LocalDate target) {
        return (target.isEqual(from) || target.isAfter(from))
                && (target.isEqual(to) || target.isBefore(to));
    }
}
