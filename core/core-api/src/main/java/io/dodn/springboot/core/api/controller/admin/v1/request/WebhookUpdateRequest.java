package io.dodn.springboot.core.api.controller.admin.v1.request;

public record WebhookUpdateRequest(String systemName, String webhookUrl, String description, int timeoutSeconds,
		int maxRetryCount) {

}
