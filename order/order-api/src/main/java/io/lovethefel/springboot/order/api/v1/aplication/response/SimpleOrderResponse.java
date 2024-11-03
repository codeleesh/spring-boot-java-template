package io.lovethefel.springboot.order.api.v1.aplication.response;

import io.dodn.springboot.storage.db.order.entity.Address;
import io.dodn.springboot.storage.db.order.entity.Order;
import io.dodn.springboot.storage.db.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record SimpleOrderResponse(

        Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    public static SimpleOrderResponse from(final Order order) {

        return new SimpleOrderResponse(order.getId(), order.getMemberOrder().getFullName(), order.getOrderDate(),
                order.getStatus(), order.getDelivery().getAddress());
    }
}
