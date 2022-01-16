package com.bluedelivery.order.interfaces;

import static com.bluedelivery.Fixtures.*;
import static com.bluedelivery.common.response.HttpResponse.SUCCESS;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.bluedelivery.authentication.interfaces.AuthenticatedUserArgumentResolver;
import com.bluedelivery.authentication.application.AuthenticationService;
import com.bluedelivery.authentication.domain.Authentication;
import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.order.domain.OrderItem;
import com.bluedelivery.order.adapter.in.web.PlaceOrderController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PlaceOrderController.class)
public class PlaceOrderControllerTest {

    private static String VALID_TOKEN = "valid token";
    private static Long VALID_USER_ID = 1L;
    private static Long SHOP_ID = 1L;
    private static Long ORDER_ID = 1L;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private PlaceOrderUseCase placeOrderUsecase;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Authentication authentication = new Authentication(VALID_TOKEN, VALID_USER_ID);


    @Test
    void orderTest() throws Exception {
        //given
        given(authenticationService.getAuthentication(VALID_TOKEN)).willReturn(Optional.ofNullable(authentication));
        given(placeOrderUsecase.placeOrder(orderForm().build())).willReturn(order().orderId(ORDER_ID).build());

        //when
        ResultActions perform = mockMvc.perform(post("/orders")
                .content(objectMapper.writeValueAsString(
                                cart().shopId(SHOP_ID).build()))
                .header(AUTHORIZATION, VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/orders/" + ORDER_ID))
                .andExpect(jsonPath("$.result").value(SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

}
