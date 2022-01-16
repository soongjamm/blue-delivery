package com.bluedelivery.order.application.port.out;

import com.bluedelivery.order.domain.Order;

import java.util.Optional;

public interface LoadOrderPort {
    Optional<Order> getOrderById(Long orderId);
    long count();
}
