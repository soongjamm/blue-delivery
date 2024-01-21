package com.bluedelivery.shop.domain.holiday;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class WeeklyRegularHolidayPolicyTest {

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void weeklyRegularHolidaysTest(DayOfWeek dayOfWeek) {
        //given
        List<LocalDate> dates = Stream.iterate(
                        LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(1, dayOfWeek)),
                        date -> date.plusWeeks(1))
                .limit(10).collect(Collectors.toList());

        WeeklyRegularHolidayPolicy sut = new WeeklyRegularHolidayPolicy(dayOfWeek);

        dates.forEach(
                x -> {
                    // when
                    boolean holiday = sut.isHoliday(x);

                    // then
                    assertThat(holiday).isTrue();
                }
        );
    }
}
