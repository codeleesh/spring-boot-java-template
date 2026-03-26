package io.dodn.springboot.core.api.controller.admin.v1.request;

public record WebhookCreateRequest(String systemName, String webhookUrl, String description, Integer timeoutSeconds,
		Integer maxRetryCount) {

	public int timeoutSecondsOrDefault() {
		return timeoutSeconds != null ? timeoutSeconds : 5;
	}

	public int maxRetryCountOrDefault() {
		return maxRetryCount != null ? maxRetryCount : 3;
	}

}
