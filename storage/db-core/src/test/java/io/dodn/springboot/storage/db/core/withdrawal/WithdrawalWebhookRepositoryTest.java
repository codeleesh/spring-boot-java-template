package io.dodn.springboot.storage.db.core.withdrawal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dodn.springboot.storage.db.CoreDbContextTest;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class WithdrawalWebhookRepositoryTest extends CoreDbContextTest {

	private final WithdrawalWebhookRepository webhookRepository;

	public WithdrawalWebhookRepositoryTest(WithdrawalWebhookRepository webhookRepository) {
		this.webhookRepository = webhookRepository;
	}

	@Test
	public void shouldSaveAndFind() {
		WithdrawalWebhookEntity entity = new WithdrawalWebhookEntity("TestSystem",
				"http://test-system/api/validate", "Test description", 5, 3);
		WithdrawalWebhookEntity saved = webhookRepository.save(entity);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getSystemName()).isEqualTo("TestSystem");
		assertThat(saved.getWebhookUrl()).isEqualTo("http://test-system/api/validate");
		assertThat(saved.isActive()).isTrue();

		WithdrawalWebhookEntity found = webhookRepository.findById(saved.getId()).get();
		assertThat(found.getSystemName()).isEqualTo("TestSystem");
	}

	@Test
	public void shouldFindByActiveTrue() {
		webhookRepository.save(
				new WithdrawalWebhookEntity("ActiveSystem", "http://active/validate", "Active", 5, 3));
		WithdrawalWebhookEntity inactive = new WithdrawalWebhookEntity("InactiveSystem",
				"http://inactive/validate", "Inactive", 5, 3);
		inactive.changeActive(false);
		webhookRepository.save(inactive);

		List<WithdrawalWebhookEntity> actives = webhookRepository.findByActiveTrue();

		assertThat(actives).allMatch(WithdrawalWebhookEntity::isActive);
		assertThat(actives.stream().map(WithdrawalWebhookEntity::getSystemName).toList())
			.contains("ActiveSystem");
	}

	@Test
	public void shouldFindBySystemName() {
		webhookRepository
			.save(new WithdrawalWebhookEntity("UniqueSystem", "http://unique/validate", "desc", 5, 3));

		assertThat(webhookRepository.findBySystemName("UniqueSystem")).isPresent();
		assertThat(webhookRepository.findBySystemName("NonExistent")).isEmpty();
	}

	@Test
	public void shouldEnforceUniqueSystemName() {
		webhookRepository
			.save(new WithdrawalWebhookEntity("DuplicateSystem", "http://dup1/validate", "desc1", 5, 3));

		assertThatThrownBy(() -> {
			webhookRepository
				.save(new WithdrawalWebhookEntity("DuplicateSystem", "http://dup2/validate", "desc2", 5, 3));
			webhookRepository.flush();
		}).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	public void shouldUpdateEntity() {
		WithdrawalWebhookEntity saved = webhookRepository
			.save(new WithdrawalWebhookEntity("OriginalName", "http://original/validate", "desc", 5, 3));

		saved.update("UpdatedName", "http://updated/validate", "updated desc", 10, 2);
		webhookRepository.saveAndFlush(saved);

		WithdrawalWebhookEntity found = webhookRepository.findById(saved.getId()).get();
		assertThat(found.getSystemName()).isEqualTo("UpdatedName");
		assertThat(found.getWebhookUrl()).isEqualTo("http://updated/validate");
		assertThat(found.getTimeoutSeconds()).isEqualTo(10);
		assertThat(found.getMaxRetryCount()).isEqualTo(2);
	}

	@Test
	public void shouldDeleteEntity() {
		WithdrawalWebhookEntity saved = webhookRepository
			.save(new WithdrawalWebhookEntity("ToDelete", "http://delete/validate", "desc", 5, 3));
		Long id = saved.getId();

		webhookRepository.delete(saved);
		webhookRepository.flush();

		assertThat(webhookRepository.findById(id)).isEmpty();
	}

}
