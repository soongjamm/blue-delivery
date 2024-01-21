package com.bluedelivery.shop.domain.holiday;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CycleRegularHolidayPolicyTest {

    @ParameterizedTest
    @MethodSource("firstSundaysOfMonthIn2022")
    void holiday_first_sunday(LocalDate firstSunday) {
        // given
        CyclicRegularHolidayPolicy sut = new CyclicRegularHolidayPolicy(WeekCycle.FIRST, DayOfWeek.SUNDAY);

        // when
        boolean result = sut.isHoliday(firstSunday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> firstSundaysOfMonthIn2022() {
        return Stream.of(
                LocalDate.of(2022, 1, 2),
                LocalDate.of(2022, 2, 6),
                LocalDate.of(2022, 3, 6),
                LocalDate.of(2022, 4, 3),
                LocalDate.of(2022, 5, 1),
                LocalDate.of(2022, 6, 5),
                LocalDate.of(2022, 7, 3),
                LocalDate.of(2022, 8, 7),
                LocalDate.of(2022, 9, 4),
                LocalDate.of(2022, 10, 2),
                LocalDate.of(2022, 11, 6),
                LocalDate.of(2022, 12, 4)
        );
    }


    @ParameterizedTest
    @MethodSource("secondTuesdaysOfMonthIn2022")
    void holiday_second_tuesday(LocalDate secondTuesday) {
        // given
        CyclicRegularHolidayPolicy sut = new CyclicRegularHolidayPolicy(WeekCycle.SECOND, DayOfWeek.TUESDAY);

        // when
        boolean result = sut.isHoliday(secondTuesday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> secondTuesdaysOfMonthIn2022() {
        return Stream.of(
                LocalDate.of(2022, 1, 4),
                LocalDate.of(2022, 2, 8),
                LocalDate.of(2022, 3, 8),
                LocalDate.of(2022, 4, 5),
                LocalDate.of(2022, 5, 3),
                LocalDate.of(2022, 6, 7),
                LocalDate.of(2022, 7, 5),
                LocalDate.of(2022, 8, 9),
                LocalDate.of(2022, 9, 6),
                LocalDate.of(2022, 10, 4),
                LocalDate.of(2022, 11, 8),
                LocalDate.of(2022, 12, 6)
        );
    }


    @ParameterizedTest
    @MethodSource("thirdWednesdayOfMonthIn2022")
    void holiday_third_wednesday(LocalDate thirdWednesday) {
        // given
        CyclicRegularHolidayPolicy sut = new CyclicRegularHolidayPolicy(WeekCycle.THIRD, DayOfWeek.WEDNESDAY);

        // when
        boolean result = sut.isHoliday(thirdWednesday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> thirdWednesdayOfMonthIn2022() {
        return Stream.of(
                LocalDate.of(2022, 1, 12),
                LocalDate.of(2022, 2, 16),
                LocalDate.of(2022, 3, 16),
                LocalDate.of(2022, 4, 13),
                LocalDate.of(2022, 5, 11),
                LocalDate.of(2022, 6, 15),
                LocalDate.of(2022, 7, 13),
                LocalDate.of(2022, 8, 17),
                LocalDate.of(2022, 9, 14),
                LocalDate.of(2022, 10, 12),
                LocalDate.of(2022, 11, 16),
                LocalDate.of(2022, 12, 14)
        );
    }


    @ParameterizedTest
    @MethodSource("fourthThursdaysOfMonthIn2022")
    void holiday_fourth_Thursday(LocalDate fourthThursday) {
        // given
        CyclicRegularHolidayPolicy sut = new CyclicRegularHolidayPolicy(WeekCycle.FOURTH, DayOfWeek.THURSDAY);

        // when
        boolean result = sut.isHoliday(fourthThursday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> fourthThursdaysOfMonthIn2022() {
        return Stream.of(
                LocalDate.of(2022, 1, 20),
                LocalDate.of(2022, 2, 24),
                LocalDate.of(2022, 3, 24),
                LocalDate.of(2022, 4, 21),
                LocalDate.of(2022, 5, 19),
                LocalDate.of(2022, 6, 23),
                LocalDate.of(2022, 7, 21),
                LocalDate.of(2022, 8, 25),
                LocalDate.of(2022, 9, 22),
                LocalDate.of(2022, 10, 20),
                LocalDate.of(2022, 11, 24),
                LocalDate.of(2022, 12, 22)
        );
    }


    @ParameterizedTest
    @MethodSource("lastMondaysOfMonthIn2022")
    void holiday_last_monday(LocalDate lastMonday) {
        // given
        CyclicRegularHolidayPolicy sut = new CyclicRegularHolidayPolicy(WeekCycle.LAST, DayOfWeek.MONDAY);

        // when
        boolean result = sut.isHoliday(lastMonday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> lastMondaysOfMonthIn2022() {
        return Stream.of(
                LocalDate.of(2022, 1, 31),
                LocalDate.of(2022, 2, 28),
                LocalDate.of(2022, 3, 28),
                LocalDate.of(2022, 4, 25),
                LocalDate.of(2022, 5, 30),
                LocalDate.of(2022, 6, 27),
                LocalDate.of(2022, 7, 25),
                LocalDate.of(2022, 8, 29),
                LocalDate.of(2022, 9, 26),
                LocalDate.of(2022, 10, 31),
                LocalDate.of(2022, 11, 28),
                LocalDate.of(2022, 12, 26)
        );
    }

}
