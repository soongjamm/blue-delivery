package com.bluedelivery.shop.infra.holiday;

import com.bluedelivery.shop.domain.holiday.CyclicRegularHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.HolidayPolicy;
import com.bluedelivery.shop.domain.holiday.TemporaryHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.WeekCycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HolidayPolicyListJsonConverterTest {

    private HolidayPolicyListJsonConverter sut;

    @BeforeEach
    void setUp() {
        sut = new HolidayPolicyListJsonConverter();
    }

    @Test
    void convertToDatabaseColumnTest() {
        String result = sut.convertToDatabaseColumn(
                List.of(
                        new CyclicRegularHolidayPolicy(WeekCycle.FIRST, DayOfWeek.SUNDAY),
                        new CyclicRegularHolidayPolicy(WeekCycle.LAST, DayOfWeek.MONDAY)
                )
        );

        assertThat(result).isEqualTo("[{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"FIRST\",\"dayOfWeek\":\"SUNDAY\"},{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"}]");
    }

    @Test
    void convertToEntityAttributeTest() {
        List<HolidayPolicy> results = sut.convertToEntityAttribute("[{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"FIRST\",\"dayOfWeek\":\"SUNDAY\"},{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"},{\"@type\":\"TemporaryHolidayPolicy\",\"from\":[999999999,12,30],\"to\":[999999999,12,31]}]");

        assertThat(results).hasSize(3);
        assertThat(results.get(0)).isInstanceOf(CyclicRegularHolidayPolicy.class);
        assertThat(results.get(1)).isInstanceOf(CyclicRegularHolidayPolicy.class);
        assertThat(results.get(2)).isInstanceOf(TemporaryHolidayPolicy.class);
    }

}