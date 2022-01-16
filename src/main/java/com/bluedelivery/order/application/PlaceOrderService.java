package com.bluedelivery.order.application;

import com.bluedelivery.common.event.EventEnvelope;
import com.bluedelivery.order.adapter.out.persistence.OrderRepositoryJpa;
import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.application.port.out.SaveOrderPort;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.order.domain.OrderCreatedEvent;
import com.bluedelivery.payment.Payment;
import com.bluedelivery.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlaceOrderService implements PlaceOrderUseCase {
    
    private final SaveOrderPort saveOrderPort;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Order placeOrder(Order.OrderForm form) {
        Order order = orderMapper.map(form);
        order.pay(paymentService.process(new Payment.PaymentForm(order)));
        saveOrderPort.save(order);
        publisher.publishEvent(EventEnvelope.builder()
                .aggregateId(order.getOrderId())
                .aggregateType(Order.class.getSimpleName())
                .event(OrderCreatedEvent.from(order))
                .eventType(OrderCreatedEvent.class.getSimpleName())
                .build());
        return order;
    }
    
}
