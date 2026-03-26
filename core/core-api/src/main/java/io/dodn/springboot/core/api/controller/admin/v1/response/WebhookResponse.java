package io.dodn.springboot.core.api.controller.admin.v1.response;

import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import java.time.LocalDateTime;

public record WebhookResponse(Long id, String systemName, String webhookUrl, String description, int timeoutSeconds,
		int maxRetryCount, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {

	public static WebhookResponse from(WithdrawalWebhookEntity entity) {
		return new WebhookResponse(entity.getId(), entity.getSystemName(), entity.getWebhookUrl(),
				entity.getDescription(), entity.getTimeoutSeconds(), entity.getMaxRetryCount(), entity.isActive(),
				entity.getCreatedAt(), entity.getUpdatedAt());
	}

}
