package io.lovethefel.springboot.order.api.v1.controller;

import io.lovethefel.springboot.order.api.v1.aplication.OrderApiService;
import io.lovethefel.springboot.order.api.v1.aplication.response.SimpleOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiContoller {

    private OrderApiService orderApiService;

    @GetMapping("/api/simple-orders")
    public List<SimpleOrderResponse> orders() {

        return orderApiService.orders();
    }

}
