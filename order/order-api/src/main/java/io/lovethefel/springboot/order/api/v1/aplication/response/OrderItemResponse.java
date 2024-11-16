package io.lovethefel.springboot.order.api.v1.aplication.response;

import io.dodn.springboot.storage.db.order.entity.OrderItem;

public record OrderItemResponse(

        String itemName,
        int orderPrice,
        int count
) {

    public static OrderItemResponse from(final OrderItem orderItem) {

        return new OrderItemResponse(orderItem.getItem().getName(), orderItem.getOrderPrice(), orderItem.getCount());
    }
}
