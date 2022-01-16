package com.bluedelivery.order.adapter.in.web;

import static com.bluedelivery.common.response.HttpResponse.response;

import java.net.URI;

import com.bluedelivery.authentication.interfaces.AuthenticationRequired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluedelivery.common.response.HttpResponse;
import com.bluedelivery.authentication.domain.Authentication;
import com.bluedelivery.order.application.port.in.PlaceOrderUseCase;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.order.interfaces.Cart;

@Api(tags = "주문")
@RequestMapping("/orders")
@RestController
public class PlaceOrderController {
    
    private final PlaceOrderUseCase placeOrderUsecase;

    public PlaceOrderController(PlaceOrderUseCase placeOrderUsecase) {
        this.placeOrderUsecase = placeOrderUsecase;
    }

    @ApiOperation(value = "주문 생성 요청", notes = "인증 정보와 장바구니 정보를 바탕으로 주문을 생성한다.")
    @PostMapping
    @AuthenticationRequired
    public ResponseEntity<HttpResponse<?>> createOrderRequest(Authentication authentication, @RequestBody Cart cart) {
        Order order = placeOrderUsecase.placeOrder(cart.toOrderForm(authentication.getUserId()));
        return ResponseEntity.created(URI.create("/orders/" + order.getOrderId())).body(response(order));
    }
}
