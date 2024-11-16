package io.lovethefel.springboot.order.api.v1.aplication;

import io.dodn.springboot.storage.db.order.entity.Order;
import io.dodn.springboot.storage.db.order.repository.OrderRepository;
import io.dodn.springboot.storage.db.order.repository.dto.OrderSimpleQueryDto;
import io.lovethefel.springboot.order.api.v1.aplication.response.OrderResponse;
import io.lovethefel.springboot.order.api.v1.aplication.response.SimpleOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderApiService {

    private final OrderRepository orderRepository;

    public List<SimpleOrderResponse> simpleOrders() {

        final List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderResponse::from)
                .toList();
    }

    public List<SimpleOrderResponse> simpleOrdersV2() {

        final List<OrderSimpleQueryDto> orderDtos = orderRepository.findOrderDtos();
        return orderDtos.stream().map(SimpleOrderResponse::from).toList();
    }

    public List<OrderResponse> orders() {

        final List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    public List<OrderResponse> ordersV2() {

        final List<Order> orders = orderRepository.finaAllWithItem();
        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }
}
