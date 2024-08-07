package com.bluedelivery.shop.interfaces;

import static com.bluedelivery.common.response.HttpResponse.SUCCESS;
import static com.bluedelivery.shop.application.businesshour.BusinessHourType.EVERY_SAME_TIME;
import static com.bluedelivery.shop.interfaces.dto.BusinessHourDay.EVERY_DAY;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bluedelivery.shop.domain.holiday.WeekCycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.bluedelivery.address.domain.CityToDong;
import com.bluedelivery.category.application.CategoryManagerService;
import com.bluedelivery.common.config.GlobalExceptionHandler;
import com.bluedelivery.shop.application.AddressMapper;
import com.bluedelivery.shop.application.RegularClosingParam;
import com.bluedelivery.shop.application.ShopUpdateService;
import com.bluedelivery.shop.application.TemporaryClosingParam;
import com.bluedelivery.shop.application.businesshour.DayOfWeekMapper;
import com.bluedelivery.shop.application.businesshour.EverydayBusinessHourCondition;
import com.bluedelivery.shop.application.businesshour.WeekdayWeekendBusinessHourCondition;
import com.bluedelivery.shop.application.dto.BusinessHourParam;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.domain.DeliveryArea;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;
import com.bluedelivery.shop.interfaces.adapter.ShopUpdateControllerImpl;
import com.bluedelivery.shop.interfaces.dto.BusinessHourDay;
import com.bluedelivery.shop.interfaces.dto.BusinessHoursRequest;
import com.bluedelivery.shop.interfaces.dto.UpdateDeliveryAreaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class ShopUpdateControllerTest {
    
    @Mock
    ShopRepository shopRepository;
    @Mock
    CategoryManagerService categoryManagerService;
    @Mock
    AddressMapper addressMapper;
    
    private DayOfWeekMapper mapper = new DayOfWeekMapper(List.of(
            new EverydayBusinessHourCondition(),
            new WeekdayWeekendBusinessHourCondition()));
    private MockMvc mvc;
    private ShopUpdateController controller;
    private ShopUpdateService service;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setup() {
        Shop shop = new Shop();
        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        service = new ShopUpdateService(shopRepository, categoryManagerService, addressMapper, mapper);
        controller = new ShopUpdateControllerImpl(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }
    
    @Test
    void updateBusinessHourTest() throws Exception {
        //given
        Map<BusinessHourDay, BusinessHourParam> hours = new LinkedHashMap<>();
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.MIDNIGHT;
        hours.put(EVERY_DAY, new BusinessHourParam(open, close));
        BusinessHoursRequest dto = new BusinessHoursRequest(EVERY_SAME_TIME, hours);
        
        //when
        MockHttpServletResponse response = mvc.perform(put("/shops/{id}/business-hours", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse();
        
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(
                objectMapper.writeValueAsString(List.of(
                        new BusinessHour(MONDAY, open, close),
                        new BusinessHour(TUESDAY, open, close),
                        new BusinessHour(WEDNESDAY, open, close),
                        new BusinessHour(THURSDAY, open, close),
                        new BusinessHour(FRIDAY, open, close),
                        new BusinessHour(SATURDAY, open, close),
                        new BusinessHour(SUNDAY, open, close)
                )));
    }
    
    @Test
    void updateDeliveryAreaTest() throws Exception {
        //given
        List<String> townCodes = List.of("1111010200", "1111010300", "1111010400");
        CityToDong shingyo = CityToDong.builder()
                .addressJurisdictionEupMyonDongCode("1111010200").eupMyonDongName("신교동").build();
        CityToDong goongjung = CityToDong.builder()
                .addressJurisdictionEupMyonDongCode("1111010300").eupMyonDongName("궁정동").build();
        CityToDong hyoja = CityToDong.builder()
                .addressJurisdictionEupMyonDongCode("1111010200").eupMyonDongName("효자동").build();
        List<DeliveryArea> deliveryAreas = List.of(
                DeliveryArea.of(shingyo), DeliveryArea.of(goongjung), DeliveryArea.of(hyoja));
        given(addressMapper.deliveryAreas(townCodes)).willReturn(deliveryAreas);
        UpdateDeliveryAreaRequest dto = new UpdateDeliveryAreaRequest(townCodes);
        
        //when
        ResultActions perform = mvc.perform(put("/shops/{id}/delivery-areas", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        
        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SUCCESS))
                .andExpect(jsonPath("$.data.deliveryAreas[0].townName").value("신교동"))
                .andExpect(jsonPath("$.data.deliveryAreas[1].townName").value("궁정동"))
                .andExpect(jsonPath("$.data.deliveryAreas[2].townName").value("효자동"));
    }
    
    @Test
    @Disabled
    void updateClosingDays() throws Exception {
        LocalDate june18 = LocalDate.of(2021, Month.JUNE, 18);
        LocalDate june23 = june18.plusDays(5);
        
        UpdateClosingDaysRequest request = new UpdateClosingDaysRequest(
                true,
                List.of(new RegularClosingParam(DayOfWeek.SUNDAY),
                        new RegularClosingParam(WeekCycle.LAST, MONDAY)),
                List.of(new TemporaryClosingParam(june18, june23))
        );
        
        mvc.perform(put("/shops/1/closing-days")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
