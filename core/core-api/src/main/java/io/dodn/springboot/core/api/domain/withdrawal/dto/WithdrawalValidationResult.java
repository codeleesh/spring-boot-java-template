package io.dodn.springboot.core.api.domain.withdrawal.dto;

import java.util.List;
import java.util.stream.Collectors;

public record WithdrawalValidationResult(List<WebhookCallResult> results) {

	public boolean isEligible() {
		return results.stream().allMatch(WebhookCallResult::isPassed);
	}

	public List<WebhookCallResult> failures() {
		return results.stream().filter(r -> !r.isPassed()).toList();
	}

	public String rejectionSummary() {
		return failures().stream()
			.map(r -> r.systemName() + ": " + r.reason())
			.collect(Collectors.joining("; "));
	}

	public static WithdrawalValidationResult empty() {
		return new WithdrawalValidationResult(List.of());
	}

}
