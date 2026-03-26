package io.dodn.springboot.storage.db.core.withdrawal;

import io.dodn.springboot.storage.db.core.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "withdrawal_webhook",
		uniqueConstraints = @UniqueConstraint(name = "uk_withdrawal_webhook_system_name", columnNames = "systemName"))
public class WithdrawalWebhookEntity extends BaseEntity {

	@Column(nullable = false, length = 100)
	private String systemName;

	@Column(nullable = false, length = 500)
	private String webhookUrl;

	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	private int timeoutSeconds = 5;

	@Column(nullable = false)
	private int maxRetryCount = 3;

	@Column(nullable = false)
	private boolean active = true;

	protected WithdrawalWebhookEntity() {
	}

	public WithdrawalWebhookEntity(String systemName, String webhookUrl, String description, int timeoutSeconds,
			int maxRetryCount) {
		this.systemName = systemName;
		this.webhookUrl = webhookUrl;
		this.description = description;
		this.timeoutSeconds = timeoutSeconds;
		this.maxRetryCount = maxRetryCount;
		this.active = true;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getWebhookUrl() {
		return webhookUrl;
	}

	public String getDescription() {
		return description;
	}

	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public boolean isActive() {
		return active;
	}

	public void update(String systemName, String webhookUrl, String description, int timeoutSeconds,
			int maxRetryCount) {
		this.systemName = systemName;
		this.webhookUrl = webhookUrl;
		this.description = description;
		this.timeoutSeconds = timeoutSeconds;
		this.maxRetryCount = maxRetryCount;
	}

	public void changeActive(boolean active) {
		this.active = active;
	}

}
