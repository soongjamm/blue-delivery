package com.bluedelivery.order.application;

import org.springframework.stereotype.Component;

import com.bluedelivery.order.domain.Order;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderMapper {
    
    private final OrderValidator orderValidator;
    
    public Order map(Order.OrderForm form) {
        Order order = Order.place(form);
        orderValidator.validate(order);
        return order;
    }
}
