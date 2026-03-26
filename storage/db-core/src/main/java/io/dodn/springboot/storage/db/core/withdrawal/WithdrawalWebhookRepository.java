package io.dodn.springboot.storage.db.core.withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WithdrawalWebhookRepository extends JpaRepository<WithdrawalWebhookEntity, Long> {

	List<WithdrawalWebhookEntity> findByActiveTrue();

	Optional<WithdrawalWebhookEntity> findBySystemName(String systemName);

	boolean existsBySystemName(String systemName);

}
