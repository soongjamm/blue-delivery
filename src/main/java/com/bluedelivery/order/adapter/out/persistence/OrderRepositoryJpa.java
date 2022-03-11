package com.bluedelivery.order.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bluedelivery.order.domain.Order;

@Repository
public interface OrderRepositoryJpa extends JpaRepository<Order, Long> {
    
    @Query("select o from Order o join fetch o.orderItems oi where o.orderId = :orderId")
    Optional<Order> getFlatOrderById(Long orderId);
}
