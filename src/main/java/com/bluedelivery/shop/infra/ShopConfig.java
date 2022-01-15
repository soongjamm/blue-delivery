package com.bluedelivery.shop.infra;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bluedelivery.shop.application.businesshour.BusinessHourCondition;
import com.bluedelivery.shop.application.businesshour.EverydayBusinessHourCondition;
import com.bluedelivery.shop.application.businesshour.WeekdayWeekendBusinessHourCondition;

@Configuration
public class ShopConfig {
    
    @Bean
    public List<BusinessHourCondition> businessHourConditions() {
        return List.of(
                new EverydayBusinessHourCondition(),
                new WeekdayWeekendBusinessHourCondition());
    }
}
