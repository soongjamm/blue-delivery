package com.bluedelivery.shop.application;

import static com.bluedelivery.shop.domain.closingday.LocalDateTimeConverter.toLocalDateTime;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bluedelivery.category.application.CategoryManagerService;
import com.bluedelivery.shop.application.businesshour.DayOfWeekMapper;
import com.bluedelivery.shop.application.businesshour.EverydayBusinessHourCondition;
import com.bluedelivery.shop.application.businesshour.WeekdayWeekendBusinessHourCondition;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;
import com.bluedelivery.shop.domain.closingday.CyclicRegularClosing;
import com.bluedelivery.shop.domain.closingday.LegalHolidayClosing;
import com.bluedelivery.shop.interfaces.UpdateClosingDaysRequest;

@ExtendWith(MockitoExtension.class)
class ShopUpdateServiceTest {
    
    ShopUpdateService service;
    Shop shop;
    private DayOfWeekMapper mapper = new DayOfWeekMapper(List.of(
            new EverydayBusinessHourCondition(),
            new WeekdayWeekendBusinessHourCondition()));
    
    @BeforeEach
    void setup(@Mock ShopRepository shopRepository,
               @Mock CategoryManagerService categoryManagerService,
               @Mock AddressMapper addressMapper) {
        shop = new Shop();
        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));
        service = new ShopUpdateService(shopRepository, categoryManagerService, addressMapper, mapper);
    }
    
    @Test
    @Disabled
    void updateClosingDays() {
        LocalDate lastMondayOnJune = LocalDate.of(2021, Month.JUNE, 28);
        LocalDate sunday = LocalDate.of(2021, Month.JUNE, 20);
        LocalDate june18 = LocalDate.of(2021, Month.JUNE, 18);
        LocalDate june23 = june18.plusDays(5);
        
        UpdateClosingDaysRequest request = new UpdateClosingDaysRequest(
                true,
                List.of(new RegularClosingParam(DayOfWeek.SUNDAY),
                        new RegularClosingParam(CyclicRegularClosing.Cycle.LAST, DayOfWeek.MONDAY)),
                List.of(new TemporaryClosingParam(june18, june23))
        );
        service.updateClosingDays(1L, request);
        
        // 법정공휴일 테스트
        LegalHolidayClosing.getHolidaysOf(Year.of(2021)).forEach(
                x -> assertThat(shop.isClosed(toLocalDateTime(x))).isTrue()
        );
        // 정기휴무일 테스트
        assertThat(shop.isClosed(toLocalDateTime(lastMondayOnJune))).isTrue();
        assertThat(shop.isClosed(toLocalDateTime(sunday))).isTrue();
        // 임시휴무일 테스트
        june18.datesUntil(june23.plusDays(1)).forEach(
                x -> assertThat(shop.isClosed(toLocalDateTime(x))).isTrue()
        );
    }
}
