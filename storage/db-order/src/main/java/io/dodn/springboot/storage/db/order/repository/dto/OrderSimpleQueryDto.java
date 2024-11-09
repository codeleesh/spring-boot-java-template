package io.dodn.springboot.storage.db.order.repository.dto;

import io.dodn.springboot.storage.db.order.entity.Address;
import io.dodn.springboot.storage.db.order.entity.Name;
import io.dodn.springboot.storage.db.order.entity.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private Name name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(final Long orderId, final Name name, final LocalDateTime orderDate
            , final OrderStatus orderStatus, final Address address) {

        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
