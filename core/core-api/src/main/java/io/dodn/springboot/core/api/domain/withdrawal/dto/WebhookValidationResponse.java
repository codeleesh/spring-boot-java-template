package io.dodn.springboot.core.api.domain.withdrawal.dto;

public record WebhookValidationResponse(boolean eligible, String reason) {

}
