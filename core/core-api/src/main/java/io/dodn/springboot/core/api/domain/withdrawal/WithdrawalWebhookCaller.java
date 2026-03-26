package io.dodn.springboot.core.api.domain.withdrawal;

import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookCallResult;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookValidationRequest;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookValidationResponse;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Component
public class WithdrawalWebhookCaller {

	private static final Logger log = LoggerFactory.getLogger(WithdrawalWebhookCaller.class);

	private final RestClient webhookRestClient;

	public WithdrawalWebhookCaller(RestClient webhookRestClient) {
		this.webhookRestClient = webhookRestClient;
	}

	public WebhookCallResult call(WithdrawalWebhookEntity webhook, Long memberId) {
		WebhookCallResult lastResult = null;
		int maxRetry = webhook.getMaxRetryCount();

		for (int attempt = 1; attempt <= maxRetry; attempt++) {
			lastResult = doCall(webhook, memberId);
			if (lastResult.isPassed()) {
				return lastResult;
			}
			log.warn("Webhook call attempt {}/{} failed for system [{}]: {}", attempt, maxRetry,
					webhook.getSystemName(), lastResult.reason());
		}

		return lastResult;
	}

	private WebhookCallResult doCall(WithdrawalWebhookEntity webhook, Long memberId) {
		try {
			WebhookValidationResponse response = webhookRestClient.post()
				.uri(webhook.getWebhookUrl())
				.body(new WebhookValidationRequest(memberId, LocalDateTime.now()))
				.retrieve()
				.body(WebhookValidationResponse.class);

			if (response == null) {
				return WebhookCallResult.error(webhook.getSystemName(), "Null response from webhook");
			}

			if (!response.eligible()) {
				return WebhookCallResult.rejected(webhook.getSystemName(), response.reason());
			}

			return WebhookCallResult.passed(webhook.getSystemName());
		}
		catch (Exception e) {
			log.error("Webhook call error for system [{}]: {}", webhook.getSystemName(), e.getMessage());
			return WebhookCallResult.error(webhook.getSystemName(), e.getMessage());
		}
	}

}
