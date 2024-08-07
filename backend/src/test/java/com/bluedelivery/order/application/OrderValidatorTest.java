package com.bluedelivery.order.application;

import static com.bluedelivery.Fixtures.*;
import static com.bluedelivery.order.domain.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bluedelivery.menu.domain.MenuRepository;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.domain.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    
    @Mock(lenient = true)
    private Shop shop;
    
    @Mock(lenient = true)
    private MenuRepository menuRepository;
    
    @Mock(lenient = true)
    private UserRepository userRepository;
    
    @Mock(lenient = true)
    private ShopRepository shopRepository;
    
    private OrderValidator orderValidator;
    
    @BeforeEach
    void setup() {
        orderValidator = new OrderValidator(menuRepository, shopRepository, userRepository);
        given(userRepository.findById(1L)).willReturn(Optional.of(new User()));
        given(shopRepository.findById(1L)).willReturn(Optional.of(shop));
        given(menuRepository.findAllById(List.of(1L))).willReturn(List.of(menu().build()));
        given(shop.isOpen(any())).willReturn(true);
        given(shop.getMinimumOrderAmount()).willReturn(0);
    }
    
    @Test
    void exception_if_shop_is_not_open() {
        //given
        Order order = order().build();
        Shop shop = new Shop();
        shop.updateExposeStatus(false);
        given(shopRepository.findById(1L)).willReturn(Optional.of(shop));
        
        //when
        String msg = assertThrows(IllegalStateException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).contains(SHOP_IS_NOT_OPEN);
    }
    
    @Test
    void exception_if_ordered_item_list_is_empty() {
        //given
        Order order = order().orderItems(Collections.emptyList()).build();
        
        //when
        String msg = assertThrows(IllegalArgumentException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).contains(ORDER_LIST_IS_EMPTY);
    }
    
    @Test
    void exception_if_ordered_item_and_menu_are_different() {
        //given
        Order order = order().orderItems(List.of(orderItem().menuName("옛날양념통닭").build())).build();
        
        //when
        String msg = assertThrows(IllegalStateException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).contains(ORDERED_AND_MENU_ARE_DIFFERENT);
    }
    
    @Test
    void exception_if_ordered_amount_is_lower_than_minimum_amount() {
        //given
        Order order = order().build();
        Shop shop = new Shop();
        shop.setMinimumOrderAmount(30000);
        shop.updateExposeStatus(true);
        shop.updateBusinessHours(open24hour());
        given(shopRepository.findById(1L)).willReturn(Optional.of(shop));
        
        //when
        String msg = assertThrows(IllegalArgumentException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).isEqualTo(ORDERED_AMOUNT_LOWER_THAN_MINIMUM_ORDER_AMOUNT);
    }
    
    private List<BusinessHour> open24hour() {
        return List.of(
                new BusinessHour(DayOfWeek.MONDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.TUESDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.WEDNESDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.THURSDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.FRIDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.SATURDAY, LocalTime.MIN, LocalTime.MAX),
                new BusinessHour(DayOfWeek.SUNDAY, LocalTime.MIN, LocalTime.MAX));
    }
    
    @Test
    void exception_if_user_does_not_exist() {
        //given
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        Order order = order().userId(null).build();
        
        //when
        String msg = assertThrows(IllegalArgumentException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).isEqualTo(ORDER_USER_DOES_NOT_EXIST);
    }
    
    @Test
    void exception_if_order_option_is_different_with_menu_option() {
        //given
        Order order = order()
                .orderItems(List.of(
                        orderItem().orderItemOptionGroups(List.of(
                                        orderItemOptionGroup().orderItemOptions(List.of(
                                                        orderItemOption().name("마요네즈").build()))
                                                .build()))
                                .build()))
                .build();
        
        //when
        String msg = assertThrows(IllegalStateException.class, () -> orderValidator.validate(order)).getMessage();
        
        //then
        assertThat(msg).isEqualTo(ORDERED_AND_MENU_ARE_DIFFERENT);
    
    }
    
    @Test
    void success_when_order_is_validate() {
        //given
        Order order = order().build();
        
        //when //then
        assertDoesNotThrow(() -> orderValidator.validate(order));
    }

}
