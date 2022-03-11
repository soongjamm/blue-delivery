package com.bluedelivery.order.infra;

import static com.bluedelivery.Fixtures.order;
import static com.bluedelivery.Fixtures.orderForm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.bluedelivery.common.event.OutboxRepository;
import com.bluedelivery.order.application.OrderMapper;
import com.bluedelivery.order.application.PlaceOrderService;
import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.application.port.out.LoadOrderPort;
import com.bluedelivery.order.application.port.out.SaveOrderPort;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.payment.Payment;
import com.bluedelivery.payment.PaymentService;

@SpringBootTest
@ActiveProfiles("test")
class EventHandlerTest {
    
    private PlaceOrderUseCase placeOrderUsecase;
    
    @Autowired
    private SaveOrderPort saveOrderPort;

    @Autowired
    private LoadOrderPort loadOrderPort;

    @Autowired
    private OutboxRepository outboxRepository;
    
    @Mock
    private PaymentService paymentService;
    
    @Mock
    private OrderMapper orderMapper;
    
    @BeforeEach
    void setup() {
        placeOrderUsecase = new PlaceOrderService(saveOrderPort, orderMapper, paymentService);
    }

    @Test
    void if_order_succeed_then_order_and_outbox_saved_into_db() {
        //given
        Order.OrderForm form = orderForm().build();
        Order order = order().build();
        Payment.PaymentForm paymentForm = new Payment.PaymentForm(order);
        given(orderMapper.map(form)).willReturn(order);
        given(paymentService.process(paymentForm)).willReturn(paymentForm.createPayment());
        
        long originalOrderSize = loadOrderPort.count();
        long originalOutboxSize = outboxRepository.count();
        
        //when
        placeOrderUsecase.placeOrder(form);
        long ordered = loadOrderPort.count();
        long outboxes = outboxRepository.count();
        
        //then
        assertThat(ordered).isEqualTo(originalOrderSize + 1);
        assertThat(outboxes).isEqualTo(originalOutboxSize + 1);
    }
}
