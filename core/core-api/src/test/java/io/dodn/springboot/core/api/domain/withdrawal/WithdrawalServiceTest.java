package io.dodn.springboot.core.api.domain.withdrawal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dodn.springboot.core.api.domain.event.MemberWithdrawalEvent;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookCallResult;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WithdrawalValidationResult;
import io.dodn.springboot.core.enums.WithdrawalStatus;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogEntity;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

public class WithdrawalServiceTest {

	private WithdrawalValidationService validationService;

	private WithdrawalRequestLogRepository requestLogRepository;

	private ApplicationEventPublisher eventPublisher;

	private WithdrawalService withdrawalService;

	@BeforeEach
	public void setUp() {
		validationService = mock(WithdrawalValidationService.class);
		requestLogRepository = mock(WithdrawalRequestLogRepository.class);
		eventPublisher = mock(ApplicationEventPublisher.class);
		withdrawalService = new WithdrawalService(validationService, requestLogRepository, eventPublisher);

		when(requestLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
	}

	@Test
	public void shouldCompleteWhenValidationPasses() {
		when(validationService.validate(1L)).thenReturn(new WithdrawalValidationResult(
				List.of(WebhookCallResult.passed("System1"), WebhookCallResult.passed("System2"))));

		WithdrawalRequestLogEntity result = withdrawalService.withdraw(1L);

		assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.COMPLETED.name());
		verify(eventPublisher).publishEvent(any(MemberWithdrawalEvent.class));
	}

	@Test
	public void shouldRejectWhenValidationFails() {
		when(validationService.validate(1L)).thenReturn(new WithdrawalValidationResult(
				List.of(WebhookCallResult.passed("System1"),
						WebhookCallResult.rejected("System2", "Active order"))));

		WithdrawalRequestLogEntity result = withdrawalService.withdraw(1L);

		assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.REJECTED.name());
		assertThat(result.getRejectReason()).contains("System2");
		assertThat(result.getRejectReason()).contains("Active order");
		verify(eventPublisher, never()).publishEvent(any(MemberWithdrawalEvent.class));
	}

	@Test
	public void shouldCompleteWhenNoWebhooksRegistered() {
		when(validationService.validate(1L)).thenReturn(WithdrawalValidationResult.empty());

		WithdrawalRequestLogEntity result = withdrawalService.withdraw(1L);

		assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.COMPLETED.name());
		verify(eventPublisher).publishEvent(any(MemberWithdrawalEvent.class));
	}

	@Test
	public void shouldFailWhenEventPublishingFails() {
		when(validationService.validate(1L)).thenReturn(WithdrawalValidationResult.empty());
		org.mockito.Mockito.doThrow(new RuntimeException("Kafka unavailable"))
			.when(eventPublisher)
			.publishEvent(any(MemberWithdrawalEvent.class));

		WithdrawalRequestLogEntity result = withdrawalService.withdraw(1L);

		assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.FAILED.name());
		assertThat(result.getRejectReason()).contains("Kafka unavailable");
	}

}
