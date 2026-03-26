package io.dodn.springboot.core.api.controller.v1.response;

import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogEntity;

public record WithdrawalResponse(Long requestId, String status, String message) {

	public static WithdrawalResponse from(WithdrawalRequestLogEntity log) {
		return new WithdrawalResponse(log.getId(), log.getStatus(), log.getRejectReason());
	}

}
