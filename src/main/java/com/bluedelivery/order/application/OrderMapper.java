package com.bluedelivery.order.application;

import com.bluedelivery.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
