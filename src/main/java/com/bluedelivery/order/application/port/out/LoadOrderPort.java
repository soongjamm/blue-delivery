package com.bluedelivery.order.application.port.out;

import java.util.Optional;

import com.bluedelivery.order.domain.Order;

public interface LoadOrderPort {
    Optional<Order> getOrderById(Long orderId);

    long count();
}
