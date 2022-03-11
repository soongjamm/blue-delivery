package com.bluedelivery.order.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.application.port.out.SaveOrderPort;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.payment.Payment;
import com.bluedelivery.payment.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlaceOrderService implements PlaceOrderUseCase {
    
    private final SaveOrderPort saveOrderPort;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;

    @Transactional
    public Order placeOrder(Order.OrderForm form) {
        Order order = orderMapper.map(form);
        order.pay(paymentService.process(new Payment.PaymentForm(order)));
        saveOrderPort.save(order);
        return order;
    }
    
}
