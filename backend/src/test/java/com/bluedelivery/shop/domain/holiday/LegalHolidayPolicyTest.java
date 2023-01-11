package com.bluedelivery.shop.domain.holiday;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LegalHolidayPolicyTest {

    @ParameterizedTest
    @MethodSource("legalHolidays")
    void legalHolidaysTest(LocalDate lunarHoliday) {
        //given
        LegalHolidayPolicy sut = new LegalHolidayPolicy();

        // when
        boolean result = sut.isHoliday(lunarHoliday);

        // then
        assertThat(result).isTrue();
    }

    private static Stream<LocalDate> legalHolidays() {
        return Stream.of(
                // 2021년 신정(양력 1월 1일) 음력일
                LocalDate.of(2021, 2, 12),

                // 2021년 구정 양력일
                LocalDate.of(2021, 2, 11),
                LocalDate.of(2021, 2, 12),
                LocalDate.of(2021, 2, 13),
                LocalDate.of(2021, 2, 15),

                // 2020년 구정 양력일
                LocalDate.of(2020, 1, 24),
                LocalDate.of(2020, 1, 25),
                LocalDate.of(2020, 1, 26),
                LocalDate.of(2020, 1, 27),

                // 2019년 구정 양력일
                LocalDate.of(2019, 2, 4),
                LocalDate.of(2019, 2, 5),
                LocalDate.of(2019, 2, 6),

                // 대체 공휴일
                LocalDate.of(2021, 2, 15), // 2월 11일(목)~2월 13일(토) 설날 연휴 -> 15(월)
                LocalDate.of(2021, 6, 7), // 6월 6일(일) 현충일 -> 7(월)
                LocalDate.of(2021, 8, 16), // 8월 15일(일) 광복절 -> 16(월)
                LocalDate.of(2021, 10, 4) // 10월 3일(일) 개천절 -> 4(월)
        );
    }
}
