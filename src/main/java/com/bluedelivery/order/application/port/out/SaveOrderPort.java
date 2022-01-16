package com.bluedelivery.order.application.port.out;

import com.bluedelivery.order.domain.Order;

public interface SaveOrderPort {
    Order save(Order order);
}
