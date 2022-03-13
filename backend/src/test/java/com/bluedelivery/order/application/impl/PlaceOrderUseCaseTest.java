package com.bluedelivery.order.application.impl;

import static com.bluedelivery.Fixtures.orderForm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bluedelivery.order.application.OrderMapper;
import com.bluedelivery.order.application.PlaceOrderService;
import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.application.port.out.SaveOrderPort;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.payment.Payment;
import com.bluedelivery.payment.PaymentService;

@ExtendWith(MockitoExtension.class)
class PlaceOrderUseCaseTest {
    
    private PlaceOrderUseCase placeOrderUsecase;
    
    @Mock
    private SaveOrderPort saveOrderPort;
    
    @Mock
    private OrderMapper orderMapper;
    
    @Mock
    private PaymentService paymentService;

    private Order.OrderForm form;
    private Order order;
    private Payment.PaymentForm paymentForm;
    private Payment payment;
    
    @BeforeEach
    void setup() {
        placeOrderUsecase = new PlaceOrderService(saveOrderPort, orderMapper, paymentService);
        form = orderForm().build();
        order = Order.place(form);
        paymentForm = new Payment.PaymentForm(order);
        payment = paymentForm.createPayment();
        given(orderMapper.map(form)).willReturn(order);
        given(paymentService.process(paymentForm)).willReturn(payment);
    }
    
    @Test
    void if_payment_denied_order_fail() {
        //given
        payment.deny();
        
        //when
        assertThrows(IllegalStateException.class, () -> placeOrderUsecase.placeOrder(form));
        
        //then
        verify(saveOrderPort, never()).save(order);
    }
    
    @Test
    void takeOrderTest() {
        //given
        
        //when
        Order result = placeOrderUsecase.placeOrder(form);
        
        //then
        assertThat(result).isEqualTo(order);
        verify(saveOrderPort, times(1)).save(order);
    }
}
