package io.dodn.springboot.storage.db.order.repository;

import io.dodn.springboot.storage.db.order.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
