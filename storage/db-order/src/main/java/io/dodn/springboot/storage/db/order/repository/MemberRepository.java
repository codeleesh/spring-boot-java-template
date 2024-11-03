package io.dodn.springboot.storage.db.order.repository;

import io.dodn.springboot.storage.db.order.entity.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberOrder, Long> {

}
