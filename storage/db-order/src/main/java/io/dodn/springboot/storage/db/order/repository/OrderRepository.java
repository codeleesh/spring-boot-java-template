package io.dodn.springboot.storage.db.order.repository;

import io.dodn.springboot.storage.db.order.entity.Order;
import io.dodn.springboot.storage.db.order.repository.dto.OrderSimpleQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
                SELECT o
                  FROM Order o
                  JOIN FETCH o.member m
                  JOIN FETCH o.delivery d
            """)
    List<Order> findAllWithMemberDelivery();

    @Query(value = """
                SELECT new io.dodn.springboot.storage.db.order.repository.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)
                  FROM Order o 
                  JOIN o.member m 
                  JOIN o.delivery d
    """)
    List<OrderSimpleQueryDto> findOrderDtos();

    @Query(value = """
                SELECT o 
                  FROM Order o
                  JOIN FETCH o.member m
                  JOIN FETCH o.delivery d
                  JOIN FETCH o.orderItems oi
                  JOIN FETCH oi.item i 
    """)
    List<Order> finaAllWithItem();
}
