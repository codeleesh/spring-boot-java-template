package io.dodn.springboot.core.api.domain.withdrawal.dto;

public record WebhookCallResult(String systemName, Status status, String reason) {

	public enum Status {

		PASSED, REJECTED, ERROR, TIMEOUT

	}

	public static WebhookCallResult passed(String systemName) {
		return new WebhookCallResult(systemName, Status.PASSED, null);
	}

	public static WebhookCallResult rejected(String systemName, String reason) {
		return new WebhookCallResult(systemName, Status.REJECTED, reason);
	}

	public static WebhookCallResult error(String systemName, String reason) {
		return new WebhookCallResult(systemName, Status.ERROR, reason);
	}

	public static WebhookCallResult timeout(String systemName) {
		return new WebhookCallResult(systemName, Status.TIMEOUT, "Webhook call timed out");
	}

	public boolean isPassed() {
		return status == Status.PASSED;
	}

}
