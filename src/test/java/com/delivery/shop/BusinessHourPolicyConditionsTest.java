package com.delivery.shop;

import static com.delivery.shop.businesshour.UpdateBusinessHoursDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.delivery.shop.businesshour.BusinessHour;
import com.delivery.shop.businesshour.BusinessHourConditions;
import com.delivery.shop.businesshour.BusinessHourPolicy;
import com.delivery.shop.businesshour.BusinessHourRequestParam;
import com.delivery.shop.businesshour.BusinessHourRequestParams;
import com.delivery.shop.businesshour.UpdateBusinessHoursDto;

class BusinessHourPolicyConditionsTest {
    
    BusinessHourConditions policies = new BusinessHourConditions();
    BusinessHourRequestParams params;
    
    @Test
    @DisplayName("타입 EVERYDAY만 갖는 BusinessHour를 넘겨주면 모든 요일이 동일한 영업시간을 생성한다.")
    void everydayBusinessHourTest() {
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(20, 0);
        HashMap<DayType, BusinessHourRequestParam> map = new HashMap<>();
        map.put(DayType.EVERYDAY, new BusinessHourRequestParam(open, close));
        this.params = new BusinessHourRequestParams(map);
        
        BusinessHourPolicy businessHourPolicy = policies.makeBusinessHoursBy(1L,
                new UpdateBusinessHoursDto(BusinessHourType.EVERY_SAME_TIME, this.params));
        for (DayOfWeek day : DayOfWeek.values()) {
            BusinessHour hour = businessHourPolicy.getBusinessHourOf(day);
            assertThat(hour.getOpen()).isEqualTo(open);
            assertThat(hour.getClose()).isEqualTo(close);
            System.out.println(hour + " on " + day);
        }
    }
    
    @Test
    @DisplayName("타입 WEEKDAY, SATURDAY, SUNDAY만 넘겨주면 평일, 토요일, 일요일별 영업 시간을 생성한다.")
    void weekdayWeekendBusinessHourTest() {
        LocalTime weekdayOpen = LocalTime.of(9, 0);
        LocalTime weekdayClose = LocalTime.of(20, 0);
        LocalTime satOpen = LocalTime.of(11, 0);
        LocalTime satClose = LocalTime.of(21, 0);
        LocalTime sunOpen = LocalTime.of(19, 0);
        LocalTime sunClose = LocalTime.of(23, 58);
        HashMap<DayType, BusinessHourRequestParam> map = new HashMap<>();
        map.put(DayType.WEEKDAY, new BusinessHourRequestParam(weekdayOpen, weekdayClose));
        map.put(DayType.SATURDAY, new BusinessHourRequestParam(satOpen, satClose));
        map.put(DayType.SUNDAY, new BusinessHourRequestParam(sunOpen, sunClose));
        this.params = new BusinessHourRequestParams(map);
        
        BusinessHourPolicy businessHourPolicy = policies.makeBusinessHoursBy(1L,
                new UpdateBusinessHoursDto(BusinessHourType.WEEKDAY_SAT_SUNDAY, this.params));
        for (DayOfWeek day : DayOfWeek.values()) {
            BusinessHour hour = businessHourPolicy.getBusinessHourOf(day);
            if (day.compareTo(DayOfWeek.FRIDAY) <= 0) {
                assertThat(hour.getOpen()).isEqualTo(weekdayOpen);
                assertThat(hour.getClose()).isEqualTo(weekdayClose);
            } else if (day.equals(DayOfWeek.SATURDAY)) {
                assertThat(hour.getOpen()).isEqualTo(satOpen);
                assertThat(hour.getClose()).isEqualTo(satClose);
            } else {
                assertThat(hour.getOpen()).isEqualTo(sunOpen);
                assertThat(hour.getClose()).isEqualTo(sunClose);
            }
            System.out.println(hour + " on " + day);
        }
    }
    
    @Test
    void mixedTypeFailTest() {
        LocalTime everydayOpen = LocalTime.of(9, 0);
        LocalTime everydayClose = LocalTime.of(20, 0);
        LocalTime satOpen = LocalTime.of(11, 0);
        LocalTime satClose = LocalTime.of(21, 0);
        LocalTime sunOpen = LocalTime.of(19, 0);
        LocalTime sunClose = LocalTime.of(23, 58);
        HashMap<DayType, BusinessHourRequestParam> map = new HashMap<>();
        map.put(DayType.EVERYDAY, new BusinessHourRequestParam(everydayOpen, everydayClose));
        map.put(DayType.SATURDAY, new BusinessHourRequestParam(satOpen, satClose));
        map.put(DayType.SUNDAY, new BusinessHourRequestParam(sunOpen, sunClose));
        this.params = new BusinessHourRequestParams(map);
        
        assertThrows(IllegalArgumentException.class,
                () -> policies.makeBusinessHoursBy(1L,
                        new UpdateBusinessHoursDto(BusinessHourType.EVERY_SAME_TIME, this.params))
        );
    }
    
}