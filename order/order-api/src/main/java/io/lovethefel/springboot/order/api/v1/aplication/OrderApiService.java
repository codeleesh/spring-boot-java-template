package io.lovethefel.springboot.order.api.v1.aplication;

import io.dodn.springboot.storage.db.order.entity.Order;
import io.dodn.springboot.storage.db.order.repository.OrderRepository;
import io.lovethefel.springboot.order.api.v1.aplication.response.SimpleOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderApiService {

    private final OrderRepository orderRepository;

    public List<SimpleOrderResponse> orders() {

        final List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(SimpleOrderResponse::from).toList();
    }

}
