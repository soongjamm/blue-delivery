package com.bluedelivery.order.adapter.out.persistence;

import com.bluedelivery.order.application.port.out.LoadOrderPort;
import com.bluedelivery.order.application.port.out.SaveOrderPort;
import com.bluedelivery.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements LoadOrderPort, SaveOrderPort {

    private final OrderRepositoryJpa orderRepositoryJpa;

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepositoryJpa.getFlatOrderById(orderId);
    }

    @Override
    public long count() {
        return orderRepositoryJpa.count();
    }

    @Override
    public Order save(Order order) {
        return orderRepositoryJpa.save(order);
    }
}
