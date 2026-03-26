package io.dodn.springboot.storage.db.core.withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRequestLogRepository extends JpaRepository<WithdrawalRequestLogEntity, Long> {

	List<WithdrawalRequestLogEntity> findByMemberId(Long memberId);

}
