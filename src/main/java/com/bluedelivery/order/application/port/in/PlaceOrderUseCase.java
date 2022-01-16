package com.bluedelivery.order.application.port.in;

import com.bluedelivery.order.domain.Order;

public interface PlaceOrderUseCase {
    Order placeOrder(Order.OrderForm form);
}
