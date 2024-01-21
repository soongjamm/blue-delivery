package com.bluedelivery.shop.domain;

import com.bluedelivery.shop.domain.holiday.CyclicRegularHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.TemporaryHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.WeekCycle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ShopRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;

    @Test
    void save() {
        Shop shop = Shop.builder()
                .holidayPolicies(List.of(
                        new CyclicRegularHolidayPolicy(WeekCycle.FIRST, DayOfWeek.MONDAY),
                        new TemporaryHolidayPolicy(LocalDate.of(2022, 12, 30), LocalDate.of(2022, 12, 31)))
                )
                .build();

        shopRepository.save(shop);
        Shop saved = shopRepository.findById(1L).orElseThrow();

        assertThat(saved).isNotNull();
    }
}