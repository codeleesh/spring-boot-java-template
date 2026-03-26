package io.dodn.springboot.core.api.domain.withdrawal;

import io.dodn.springboot.core.api.support.error.CoreApiException;
import io.dodn.springboot.core.api.support.error.ErrorType;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WithdrawalWebhookService {

	private final WithdrawalWebhookRepository webhookRepository;

	public WithdrawalWebhookService(WithdrawalWebhookRepository webhookRepository) {
		this.webhookRepository = webhookRepository;
	}

	@Transactional
	public WithdrawalWebhookEntity create(String systemName, String webhookUrl, String description,
			int timeoutSeconds, int maxRetryCount) {
		if (webhookRepository.existsBySystemName(systemName)) {
			throw new CoreApiException(ErrorType.WITHDRAWAL_WEBHOOK_DUPLICATE);
		}
		WithdrawalWebhookEntity entity = new WithdrawalWebhookEntity(systemName, webhookUrl, description,
				timeoutSeconds, maxRetryCount);
		return webhookRepository.save(entity);
	}

	@Transactional(readOnly = true)
	public List<WithdrawalWebhookEntity> findAll() {
		return webhookRepository.findAll();
	}

	@Transactional(readOnly = true)
	public WithdrawalWebhookEntity findById(Long id) {
		return webhookRepository.findById(id)
			.orElseThrow(() -> new CoreApiException(ErrorType.WITHDRAWAL_WEBHOOK_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<WithdrawalWebhookEntity> findActiveWebhooks() {
		return webhookRepository.findByActiveTrue();
	}

	@Transactional
	public WithdrawalWebhookEntity update(Long id, String systemName, String webhookUrl, String description,
			int timeoutSeconds, int maxRetryCount) {
		WithdrawalWebhookEntity entity = findById(id);
		entity.update(systemName, webhookUrl, description, timeoutSeconds, maxRetryCount);
		return entity;
	}

	@Transactional
	public void delete(Long id) {
		WithdrawalWebhookEntity entity = findById(id);
		webhookRepository.delete(entity);
	}

	@Transactional
	public WithdrawalWebhookEntity changeActive(Long id, boolean active) {
		WithdrawalWebhookEntity entity = findById(id);
		entity.changeActive(active);
		return entity;
	}

}
