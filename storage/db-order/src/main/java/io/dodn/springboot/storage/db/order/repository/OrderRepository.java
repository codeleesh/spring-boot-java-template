package io.dodn.springboot.storage.db.order.repository;

import io.dodn.springboot.storage.db.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
                SELECT o
                  FROM Order o
                  JOIN FETCH o.memberOrder m
                  JOIN FETCH o.delivery d
            """)
    List<Order> findAllWithMemberDelivery();

}
