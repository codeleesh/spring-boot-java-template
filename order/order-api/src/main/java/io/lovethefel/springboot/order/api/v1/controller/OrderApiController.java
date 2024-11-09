package io.lovethefel.springboot.order.api.v1.controller;

import io.lovethefel.springboot.order.api.v1.aplication.OrderApiService;
import io.lovethefel.springboot.order.api.v1.aplication.response.SimpleOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderApiService orderApiService;

    /**
     * entity 조회 -> Response 변환
     * @return SimpleOrderResponse
     */
    @GetMapping("/api/simple-orders")
    public List<SimpleOrderResponse> orders() {

        return orderApiService.orders();
    }

    /**
     * Query Dto 조회 -> Response 변환
     * SELECT 절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트웤 용량 최적화(생각보다 미비하다?)
     * 해당 쿼리는 재사용성 떨어짐, API 스펙에 맞춘 코드
     * @return SimpleOrderResponse
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderResponse> ordersV2() {

        return orderApiService.ordersV2();
    }

}
