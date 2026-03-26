package io.dodn.springboot.core.api.domain.withdrawal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookCallResult;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WithdrawalValidationResult;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;

public class WithdrawalValidationServiceTest {

	private WithdrawalWebhookService webhookService;

	private WithdrawalWebhookCaller webhookCaller;

	private WithdrawalValidationService validationService;

	@BeforeEach
	public void setUp() {
		webhookService = mock(WithdrawalWebhookService.class);
		webhookCaller = mock(WithdrawalWebhookCaller.class);
		validationService = new WithdrawalValidationService(webhookService, webhookCaller,
				Executors.newFixedThreadPool(2));
	}

	@Test
	public void shouldPassWhenNoActiveWebhooks() {
		when(webhookService.findActiveWebhooks()).thenReturn(List.of());

		WithdrawalValidationResult result = validationService.validate(1L);

		assertThat(result.isEligible()).isTrue();
		assertThat(result.results()).isEmpty();
	}

	@Test
	public void shouldPassWhenAllWebhooksPass() {
		WithdrawalWebhookEntity webhook1 = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		WithdrawalWebhookEntity webhook2 = new WithdrawalWebhookEntity("System2", "http://s2/validate", "", 5, 3);
		when(webhookService.findActiveWebhooks()).thenReturn(List.of(webhook1, webhook2));
		when(webhookCaller.call(eq(webhook1), eq(1L))).thenReturn(WebhookCallResult.passed("System1"));
		when(webhookCaller.call(eq(webhook2), eq(1L))).thenReturn(WebhookCallResult.passed("System2"));

		WithdrawalValidationResult result = validationService.validate(1L);

		assertThat(result.isEligible()).isTrue();
		assertThat(result.results()).hasSize(2);
	}

	@Test
	public void shouldRejectWhenOneWebhookFails() {
		WithdrawalWebhookEntity webhook1 = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		WithdrawalWebhookEntity webhook2 = new WithdrawalWebhookEntity("System2", "http://s2/validate", "", 5, 3);
		when(webhookService.findActiveWebhooks()).thenReturn(List.of(webhook1, webhook2));
		when(webhookCaller.call(eq(webhook1), eq(1L))).thenReturn(WebhookCallResult.passed("System1"));
		when(webhookCaller.call(eq(webhook2), eq(1L)))
			.thenReturn(WebhookCallResult.rejected("System2", "Active order exists"));

		WithdrawalValidationResult result = validationService.validate(1L);

		assertThat(result.isEligible()).isFalse();
		assertThat(result.failures()).hasSize(1);
		assertThat(result.failures().get(0).systemName()).isEqualTo("System2");
	}

	@Test
	public void shouldRejectWhenOneWebhookTimesOut() {
		WithdrawalWebhookEntity webhook1 = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		WithdrawalWebhookEntity webhook2 = new WithdrawalWebhookEntity("System2", "http://s2/validate", "", 5, 3);
		when(webhookService.findActiveWebhooks()).thenReturn(List.of(webhook1, webhook2));
		when(webhookCaller.call(eq(webhook1), eq(1L))).thenReturn(WebhookCallResult.passed("System1"));
		when(webhookCaller.call(eq(webhook2), eq(1L))).thenReturn(WebhookCallResult.timeout("System2"));

		WithdrawalValidationResult result = validationService.validate(1L);

		assertThat(result.isEligible()).isFalse();
		assertThat(result.failures()).hasSize(1);
		assertThat(result.failures().get(0).status()).isEqualTo(WebhookCallResult.Status.TIMEOUT);
	}

	@Test
	public void shouldIncludeRejectionSummary() {
		WithdrawalWebhookEntity webhook1 = new WithdrawalWebhookEntity("OrderService", "http://s1/validate", "", 5,
				3);
		WithdrawalWebhookEntity webhook2 = new WithdrawalWebhookEntity("PaymentService", "http://s2/validate", "",
				5, 3);
		when(webhookService.findActiveWebhooks()).thenReturn(List.of(webhook1, webhook2));
		when(webhookCaller.call(eq(webhook1), eq(1L)))
			.thenReturn(WebhookCallResult.rejected("OrderService", "Pending order"));
		when(webhookCaller.call(eq(webhook2), eq(1L))).thenReturn(WebhookCallResult.timeout("PaymentService"));

		WithdrawalValidationResult result = validationService.validate(1L);

		assertThat(result.isEligible()).isFalse();
		assertThat(result.rejectionSummary()).contains("OrderService");
		assertThat(result.rejectionSummary()).contains("PaymentService");
	}

}
