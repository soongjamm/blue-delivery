package com.bluedelivery.shop.domain.holiday;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TemporaryHolidayPolicyTest {

    private TemporaryHolidayPolicy sut;

    @ParameterizedTest
    @MethodSource("holidays")
    void when_target_is_between_from_and_to_then_holiday(LocalDate between10And15) {
        // given
        sut = new TemporaryHolidayPolicy(LocalDate.of(2022, 12, 10), LocalDate.of(2022, 12, 15));

        // when
        boolean result = sut.isHoliday(between10And15);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> holidays() {
        return Stream.of(
                LocalDate.of(2022, 12, 10),
                LocalDate.of(2022, 12, 11),
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 13),
                LocalDate.of(2022, 12, 14),
                LocalDate.of(2022, 12, 15)
        );
    }

    @ParameterizedTest
    @MethodSource("businessDays")
    void when_target_is_not_between_from_and_to_then_not_holiday(LocalDate notBetween10And15) {
        // given
        sut = new TemporaryHolidayPolicy(LocalDate.of(2022, 12, 10), LocalDate.of(2022, 12, 15));

        // when
        boolean result = sut.isHoliday(notBetween10And15);

        // then
        assertThat(result).isFalse();
    }

    private static Stream<LocalDate> businessDays() {
        return Stream.of(
                LocalDate.of(2022, 12, 7),
                LocalDate.of(2022, 12, 8),
                LocalDate.of(2022, 12, 9),
                LocalDate.of(2022, 12, 16),
                LocalDate.of(2022, 12, 17),
                LocalDate.of(2022, 12, 18)
        );
    }

    @Test
    void when_from_is_before_to_then_not_throws() {
        // given
        LocalDate from = LocalDate.of(2022, 12, 1);
        LocalDate to = LocalDate.of(2022, 12, 30);

        // when, then
        assertDoesNotThrow(() -> new TemporaryHolidayPolicy(from, to));
    }

    @Test
    void when_from_is_after_to_then_throws() {
        // given
        LocalDate from = LocalDate.of(2022, 12, 30);
        LocalDate to = LocalDate.of(2022, 12, 1);

        var result = assertThatThrownBy(() -> new TemporaryHolidayPolicy(from, to));

        // then
        result.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_difference_between_from_and_to_under_30_then_not_throws() {
        // given
        LocalDate from = LocalDate.of(2022, 12, 1);
        LocalDate to = LocalDate.of(2022, 12, 31);

        // when, then
        assertDoesNotThrow(() -> new TemporaryHolidayPolicy(from, to));
    }

    @Test
    void when_difference_between_from_and_to_over_30_then_throws() {
        // given
        LocalDate from = LocalDate.of(2022, 12, 1);
        LocalDate to = LocalDate.of(2023, 1, 1);

        // when
        var result = assertThatThrownBy(() -> new TemporaryHolidayPolicy(from, to));

        // then
        result.isInstanceOf(IllegalArgumentException.class);
    }
}
