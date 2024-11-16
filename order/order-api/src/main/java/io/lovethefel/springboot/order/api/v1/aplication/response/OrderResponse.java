package io.lovethefel.springboot.order.api.v1.aplication.response;

import io.dodn.springboot.storage.db.order.entity.Address;
import io.dodn.springboot.storage.db.order.entity.Order;
import io.dodn.springboot.storage.db.order.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long orderId,
        String name,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        Address address,
        List<OrderItemResponse> orderItems
) {
    public static OrderResponse from(final Order order) {

        final List<OrderItemResponse> orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(order.getId(), order.getMember().getFullName(), order.getOrderDate()
                , order.getStatus(), order.getDelivery().getAddress(), orderItems);
    }
}
