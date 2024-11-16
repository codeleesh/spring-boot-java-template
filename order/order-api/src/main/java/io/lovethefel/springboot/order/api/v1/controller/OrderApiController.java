package io.lovethefel.springboot.order.api.v1.controller;

import io.lovethefel.springboot.order.api.v1.aplication.OrderApiService;
import io.lovethefel.springboot.order.api.v1.aplication.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderApiService orderApiService;

    @GetMapping("/api/v1/orders")
    public ResponseEntity<List<OrderResponse>> ordersV1() {

        final List<OrderResponse> orders = orderApiService.orders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/api/v2/orders")
    public ResponseEntity<List<OrderResponse>> ordersV2() {

        final List<OrderResponse> orders = orderApiService.ordersV2();
        return ResponseEntity.ok(orders);
    }
}
