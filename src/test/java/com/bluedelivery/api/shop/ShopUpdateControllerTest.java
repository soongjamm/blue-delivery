package com.bluedelivery.api.shop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bluedelivery.application.shop.RegularClosingParam;
import com.bluedelivery.application.shop.TemporaryClosingParam;
import com.bluedelivery.domain.closingday.CyclicRegularClosing;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableCaching
class ShopUpdateControllerTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    void updateClosingDays() throws Exception {
        LocalDate june18 = LocalDate.of(2021, Month.JUNE, 18);
        LocalDate june23 = june18.plusDays(5);
        
        UpdateClosingDaysRequest request = new UpdateClosingDaysRequest(
                true,
                List.of(new RegularClosingParam(DayOfWeek.SUNDAY),
                        new RegularClosingParam(CyclicRegularClosing.Cycle.LAST, DayOfWeek.MONDAY)),
                List.of(new TemporaryClosingParam(june18, june23))
        );
        
        mockMvc.perform(put("/shops/1/closing-days")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}