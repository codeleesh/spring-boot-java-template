package io.dodn.springboot.core.api.domain.withdrawal;

import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookCallResult;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WithdrawalValidationResult;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class WithdrawalValidationService {

	private static final Logger log = LoggerFactory.getLogger(WithdrawalValidationService.class);

	private final WithdrawalWebhookService webhookService;

	private final WithdrawalWebhookCaller webhookCaller;

	private final Executor webhookExecutor;

	public WithdrawalValidationService(WithdrawalWebhookService webhookService,
			WithdrawalWebhookCaller webhookCaller, @Qualifier("webhookExecutor") Executor webhookExecutor) {
		this.webhookService = webhookService;
		this.webhookCaller = webhookCaller;
		this.webhookExecutor = webhookExecutor;
	}

	public WithdrawalValidationResult validate(Long memberId) {
		List<WithdrawalWebhookEntity> activeWebhooks = webhookService.findActiveWebhooks();

		if (activeWebhooks.isEmpty()) {
			log.info("No active webhooks registered. Skipping validation for member [{}].", memberId);
			return WithdrawalValidationResult.empty();
		}

		List<CompletableFuture<WebhookCallResult>> futures = activeWebhooks.stream()
			.map(webhook -> CompletableFuture
				.supplyAsync(() -> webhookCaller.call(webhook, memberId), webhookExecutor)
				.orTimeout(webhook.getTimeoutSeconds(), TimeUnit.SECONDS)
				.exceptionally(ex -> WebhookCallResult.timeout(webhook.getSystemName())))
			.toList();

		List<WebhookCallResult> results = futures.stream().map(CompletableFuture::join).toList();

		return new WithdrawalValidationResult(results);
	}

}
