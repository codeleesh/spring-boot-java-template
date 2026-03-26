package io.dodn.springboot.core.api.domain.withdrawal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dodn.springboot.core.api.support.error.CoreApiException;
import io.dodn.springboot.core.api.support.error.ErrorType;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class WithdrawalWebhookServiceTest {

	private WithdrawalWebhookRepository webhookRepository;

	private WithdrawalWebhookService webhookService;

	@BeforeEach
	public void setUp() {
		webhookRepository = mock(WithdrawalWebhookRepository.class);
		webhookService = new WithdrawalWebhookService(webhookRepository);
	}

	@Test
	public void shouldCreateWebhook() {
		when(webhookRepository.existsBySystemName("OrderService")).thenReturn(false);
		when(webhookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		WithdrawalWebhookEntity result = webhookService.create("OrderService", "http://order/validate",
				"Order check", 5, 3);

		assertThat(result.getSystemName()).isEqualTo("OrderService");
		assertThat(result.getWebhookUrl()).isEqualTo("http://order/validate");
		verify(webhookRepository).save(any());
	}

	@Test
	public void shouldThrowWhenDuplicateSystemName() {
		when(webhookRepository.existsBySystemName("OrderService")).thenReturn(true);

		assertThatThrownBy(() -> webhookService.create("OrderService", "http://order/validate", "Order check", 5, 3))
			.isInstanceOf(CoreApiException.class)
			.satisfies(e -> assertThat(((CoreApiException) e).getErrorType())
				.isEqualTo(ErrorType.WITHDRAWAL_WEBHOOK_DUPLICATE));
	}

	@Test
	public void shouldFindById() {
		WithdrawalWebhookEntity entity = new WithdrawalWebhookEntity("OrderService", "http://order/validate",
				"desc", 5, 3);
		when(webhookRepository.findById(1L)).thenReturn(Optional.of(entity));

		WithdrawalWebhookEntity result = webhookService.findById(1L);

		assertThat(result.getSystemName()).isEqualTo("OrderService");
	}

	@Test
	public void shouldThrowWhenNotFound() {
		when(webhookRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> webhookService.findById(999L)).isInstanceOf(CoreApiException.class)
			.satisfies(e -> assertThat(((CoreApiException) e).getErrorType())
				.isEqualTo(ErrorType.WITHDRAWAL_WEBHOOK_NOT_FOUND));
	}

	@Test
	public void shouldFindActiveWebhooks() {
		WithdrawalWebhookEntity active = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		when(webhookRepository.findByActiveTrue()).thenReturn(List.of(active));

		List<WithdrawalWebhookEntity> result = webhookService.findActiveWebhooks();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getSystemName()).isEqualTo("System1");
	}

	@Test
	public void shouldDeleteWebhook() {
		WithdrawalWebhookEntity entity = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		when(webhookRepository.findById(1L)).thenReturn(Optional.of(entity));

		webhookService.delete(1L);

		verify(webhookRepository).delete(entity);
	}

	@Test
	public void shouldChangeActive() {
		WithdrawalWebhookEntity entity = new WithdrawalWebhookEntity("System1", "http://s1/validate", "", 5, 3);
		when(webhookRepository.findById(1L)).thenReturn(Optional.of(entity));

		WithdrawalWebhookEntity result = webhookService.changeActive(1L, false);

		assertThat(result.isActive()).isFalse();
	}

}
