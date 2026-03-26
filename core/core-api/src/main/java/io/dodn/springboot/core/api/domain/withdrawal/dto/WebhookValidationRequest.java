package io.dodn.springboot.core.api.domain.withdrawal.dto;

import java.time.LocalDateTime;

public record WebhookValidationRequest(Long memberId, LocalDateTime requestedAt) {

}
